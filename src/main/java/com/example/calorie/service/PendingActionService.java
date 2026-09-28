package com.example.calorie.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.example.calorie.entity.Food;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.calorie.dto.AIPayload;
import com.example.calorie.exception.ApiException;
import com.example.calorie.model.PendingAction;

@Service
public class PendingActionService {

	private static final Logger log = LoggerFactory.getLogger(PendingActionService.class);

	private final Map<UUID, PendingAction> actions = new ConcurrentHashMap<>();

//	private final NutritionCalculationService nutritionCalculationService;

	private final DailyLogService dailyLogService;
	private final FoodService foodService;
	private final MealService mealService;
	private final UserService userService;
	private final MatchingFoodService matchingFoodService;

	public PendingActionService(DailyLogService dailyLogService,
			FoodService foodService, UserService userService, MealService mealService, MatchingFoodService matchingFoodService) {

		this.dailyLogService = dailyLogService;
		this.foodService = foodService;
		this.userService = userService;
		this.mealService = mealService;
		this.matchingFoodService = matchingFoodService;
	}

	public PendingAction createAction(AIPayload payload) {

		if (payload == null) {
			throw new ApiException("Payload is required");
		}
		if (payload.getIntent() == null || payload.getIntent().isBlank()) {
			throw new ApiException("intent is required");
		}
		String message = buildConfirmationMessage(payload);
		PendingAction action = new PendingAction(UUID.randomUUID(), payload, message);
		actions.put(action.getId(), action);
		log.info("Created pending action {} for intent {}", action.getId(), payload.getIntent());
		return action;
	}

	public PendingAction getAction(UUID id) {

		return actions.get(id);
	}

	public PendingAction confirmAction(UUID id) {
		return actions.remove(id);
	}

	public void cancelAction(UUID id) {
		actions.remove(id);
		log.info("Cancelled pending action {}", id);
	}

	@Transactional
	public String executeConfirmed(UUID id) {
		PendingAction action = actions.get(id);
		if (action == null) {
			throw new ApiException("Pending action not found", org.springframework.http.HttpStatus.NOT_FOUND);
		}
		AIPayload payload = action.getPayload();

		log.info(
				"Executing pending action {} intent {}",
				id,
				payload.getIntent()
		);

		String message = executeAction(payload);

		// Remove only after successful execution
		actions.remove(id);

		return message;
	}

	private String executeAction(AIPayload payload) {

		return switch (payload.getIntent()) {

			case "LOG_FOOD" -> {
				log.info("Starting LOG_FOOD");
				dailyLogService.logFoodFromPayload(payload);
				yield "Food logged successfully.";
			}

			case "LOG_MEAL" -> {
				dailyLogService.logMealFromPayload(payload);
				yield "Meal logged successfully.";
			}

			case "CREATE_FOOD" -> {
				foodService.createFromPayload(payload.getFood());
				yield "Food created successfully.";
			}

			case "UPDATE_FOOD" -> {
				Food food = matchingFoodService.findFood(
						payload.getFood().getName()
				);

				payload.setFoodId(food.getId());
				foodService.updateFromPayload(payload);
				yield "Food updated successfully.";
			}

			case "CREATE_MEAL" -> {
				log.info("starting to create meal");
				mealService.createFromPayload(payload);
				yield "Meal created successfully.";
			}

			case "UPDATE_MEAL" -> {
				mealService.updateFromPayload(payload);
				yield "Meal updated successfully.";
			}

			case "REMOVE_LOG_ENTRY" -> {
				if (payload.getEntryId() == null) {
					throw new ApiException(
							"entryId is required for REMOVE_LOG_ENTRY"
					);
				}

				dailyLogService.removeLogEntry(
						payload.getEntryId()
				);

				yield "Log entry removed successfully.";
			}

			case "UPDATE_USER_PROFILE" -> {
				if (payload.getFields() == null ||
						payload.getFields().isEmpty()) {

					throw new ApiException(
							"fields are required for UPDATE_USER_PROFILE"
					);
				}

				userService.updateFromAiFields(
						payload.getFields()
				);

				yield "Profile updated successfully.";
			}

			default -> throw new ApiException(
					"Unsupported intent: " + payload.getIntent()
			);
		};
	}

	private String buildConfirmationMessage(AIPayload payload) {
		return switch (payload.getIntent()) {

			case "LOG_FOOD" ->
					buildLogFoodMessage(payload);

			case "LOG_MEAL" ->
					buildLogMealMessage(payload);

			case "CREATE_FOOD" ->
					buildCreateFoodMessage(payload);

			case "UPDATE_FOOD" ->
					"Update food? Confirm?";

			case "CREATE_MEAL" ->
					"Create meal? Confirm?";

			case "UPDATE_MEAL" ->
					"Update meal? Confirm?";

			case "REMOVE_LOG_ENTRY" ->
					"Remove this log entry? Confirm?";

			case "UPDATE_USER_PROFILE" ->
					buildUpdateProfileMessage(payload);

			default ->
					throw new ApiException(
							"Unsupported intent: "
									+ payload.getIntent()
					);
		};
	}

	private String buildLogMealMessage(AIPayload payload) {

		if (payload.getMealName() == null ||
				payload.getMealName().isBlank()) {

			throw new ApiException(
					"mealName is required for LOG_MEAL"
			);
		}

		if (payload.getMealType() == null) {
			throw new ApiException(
					"mealType is required for LOG_MEAL"
			);
		}

		return "Log meal '" +
				payload.getMealName() +
				"' for " +
				payload.getMealType() +
				". Confirm?";
	}

//	private String buildLogFoodMessage(AIPayload payload){
//		if(payload.getFood().getName() == null || payload.getFood().getName().isBlank()){
//			throw new ApiException("food name is required for log food");
//		}
//		return "Log food '" + payload.getFood().getName() + ".Confirm?";
//	}

	private String buildLogFoodMessage(AIPayload payload) {

		if (payload.getItems() == null || payload.getItems().isEmpty()) {
			throw new ApiException("At least one food is required for log food");
		}

		StringBuilder message = new StringBuilder("Log ");

		for (AIPayload.Item item : payload.getItems()) {

			if (item.getFoodName() == null || item.getFoodName().isBlank()) {
				throw new ApiException("Food name is required for log food");
			}

			if (item.getQuantity() == null || item.getUnit() == null) {
				throw new ApiException(
						"Quantity and unit are required for log food"
				);
			}

			message.append(item.getQuantity())
					.append(" ")
					.append(item.getUnit())
					.append(" ")
					.append(item.getFoodName())
					.append(", ");
		}

		// Remove final ", "
		message.setLength(message.length() - 2);

		if (payload.getMealType() != null) {
			message.append(" for ")
					.append(payload.getMealType().name().toLowerCase());
		}

		message.append("? Confirm?");

		return message.toString();
	}


	private String buildCreateFoodMessage(AIPayload payload) {


		var food = payload.getFood();

		StringBuilder message = new StringBuilder();

		message.append("Create food '")
				.append(food.getName())
				.append("'");

		if (food.getBrand() != null &&
				!food.getBrand().isBlank()) {

			message.append(" (")
					.append(food.getBrand())
					.append(")");
		}

		message.append(" with ")
				.append(food.getServingSize())
				.append(food.getServingUnit())
				.append(" serving, ")
				.append(food.getCalories())
				.append(" kcal, ")
				.append(food.getProteinG())
				.append("g protein, ")
				.append(food.getCarbsG())
				.append("g carbs, ")
				.append(food.getFatG())
				.append("g fat");


		message.append(". Confirm?");

		return message.toString();
	}



//	private String buildLogFoodMessage(AIPayload payload) {
//		if (payload.getItems() == null || payload.getItems().isEmpty()) {
//			throw new ApiException("items are required for LOG_FOOD");
//		}
//		StringBuilder sb = new StringBuilder();
//		for (AIPayload.Item item : payload.getItems()) {
//			if (item.getQuantity() == null || item.getUnit() == null) {
//				throw new ApiException("Quantity and unit are required to log food");
//			}
//			int calories = nutritionCalculationService.calculateCalories(item.getFoodName(), item.getQuantity(),
//					item.getUnit());
//			if (!sb.isEmpty()) {
//				sb.append("; ");
//			}
//			sb.append("Log ").append(item.getQuantity()).append(item.getUnit()).append(" ").append(item.getFoodName())
//				.append(" (").append(calories).append(" kcal)");
//		}
//		sb.append(" for ").append(payload.getMealType()).append(". Confirm?");
//		return sb.toString();
//	}

	private String buildUpdateProfileMessage(AIPayload payload) {
		if (payload.getFields() == null || payload.getFields().isEmpty()) {
			throw new ApiException("fields are required for UPDATE_USER_PROFILE");
		}
		if (payload.getFields().containsKey("weight_kg")) {
			return "Set weight to " + payload.getFields().get("weight_kg") + " kg? Confirm?";
		}
		return "Update user profile? Confirm?";
	}

}
