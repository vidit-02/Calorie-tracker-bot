package com.example.calorie.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.example.calorie.dto.AIPayload;
import com.example.calorie.dto.meal.*;
import com.example.calorie.exception.FoodNotFoundException;
import com.example.calorie.exception.MealNotFoundException;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.calorie.entity.Food;
import com.example.calorie.entity.Meal;
import com.example.calorie.entity.MealItem;
import com.example.calorie.exception.ApiException;
import com.example.calorie.repository.FoodRepository;
import com.example.calorie.repository.MealRepository;

@Service
public class MealService {
	private static final Set<String> ALLOWED_UNITS = Set.of("g", "ml","pieces");
    private static final Logger log = LoggerFactory.getLogger(MealService.class);

	private final MealRepository mealRepository;
    private final MatchingFoodService matchingFoodService;
	private final FoodRepository foodRepository;

	public MealService(MealRepository mealRepository, FoodRepository foodRepository, MatchingFoodService matchingFoodService) {
		this.mealRepository = mealRepository;
		this.foodRepository = foodRepository;
        this.matchingFoodService = matchingFoodService;
	}

	public List<MealSummaryResponse> findAll() {
		return mealRepository.findAll().stream().map(this::toDto).toList();
	}

	public MealResponse findById(UUID id) {
		MealResponse mealResponse = new MealResponse();
		Meal mealFetched = requireMeal(id);

		mealResponse.setId(mealFetched.getId());
		mealResponse.setName(mealFetched.getName());
		mealResponse.setDescription(mealFetched.getDescription());

		mealResponse.setTotalCalories(mealFetched.getTotalCalories());
		mealResponse.setTotalProteinG(mealFetched.getTotalProteinG());
		mealResponse.setTotalCarbsG(mealFetched.getTotalCarbsG());
		mealResponse.setTotalFatG(mealFetched.getTotalFatG());

		List<MealItemResponse> itemResponses = mealFetched.getItems()
				.stream()
				.map(item -> {

					MealItemResponse itemResponse = new MealItemResponse();

					itemResponse.setFoodId(item.getFood().getId());
					itemResponse.setFoodName(item.getFood().getName());
					itemResponse.setQuantity(item.getQuantity());
					itemResponse.setUnit(item.getUnit());

					return itemResponse;
				})
				.toList();

		mealResponse.setItems(itemResponses);
		return mealResponse;
	}

	@Transactional
	public MealResponse  create(CreateMealRequest dto) {
		Meal meal = new Meal();
		meal.setName(dto.getName());
		meal.setDescription(dto.getDescription());

		BigDecimal totalCalories = BigDecimal.ZERO;
		BigDecimal totalProtein = BigDecimal.ZERO;
		BigDecimal totalCarbs = BigDecimal.ZERO;
		BigDecimal totalFat = BigDecimal.ZERO;

		for (CreateMealItemRequest itemDto : dto.getItems()){

            log.info("finding matching for for :" + itemDto.getFoodName());
            Food food = matchingFoodService.findFood(itemDto.getFoodName());

			BigDecimal quantity = itemDto.getQuantity();

			MealItem mealItem = new MealItem();

			mealItem.setMeal(meal);
			mealItem.setFood(food);
			mealItem.setQuantity(quantity);
			mealItem.setUnit(food.getServingUnit());

			BigDecimal multiplier =
					quantity.divide(
							food.getServingSize(),
							6,
							RoundingMode.HALF_UP
					);

			BigDecimal calories =
					food.getCalories().multiply(multiplier);

			BigDecimal protein =
					food.getProteinG().multiply(multiplier);

			BigDecimal carbs =
					food.getCarbsG().multiply(multiplier);

			BigDecimal fat =
					food.getFatG().multiply(multiplier);



			totalCalories = totalCalories.add(calories);
			totalProtein = totalProtein.add(protein);
			totalCarbs = totalCarbs.add(carbs);
			totalFat = totalFat.add(fat);


			// 6. Add MealItem to Meal
			meal.getItems().add(mealItem);

		}
		meal.setTotalCalories(totalCalories);
		meal.setTotalProteinG(totalProtein);
		meal.setTotalCarbsG(totalCarbs);
		meal.setTotalFatG(totalFat);

		// 8. Save Meal
		Meal savedMeal = mealRepository.save(meal);
        log.info("meal created");
		// 9. Convert entity → response DTO
		return toMealResponse(savedMeal);
	}

	private MealResponse toMealResponse(Meal meal) {

		MealResponse response = new MealResponse();

		response.setId(meal.getId());
		response.setName(meal.getName());
		response.setDescription(meal.getDescription());

		response.setTotalCalories(meal.getTotalCalories());
		response.setTotalProteinG(meal.getTotalProteinG());
		response.setTotalCarbsG(meal.getTotalCarbsG());
		response.setTotalFatG(meal.getTotalFatG());

		List<MealItemResponse> items = meal.getItems()
				.stream()
				.map(this::toMealItemResponse)
				.toList();

		response.setItems(items);

        log.info("toMealResponse created");
		return response;
	}

	private MealItemResponse toMealItemResponse(MealItem item) {

		MealItemResponse response = new MealItemResponse();

		response.setFoodId(item.getFood().getId());
		response.setFoodName(item.getFood().getName());
		response.setQuantity(item.getQuantity());
		response.setUnit(item.getUnit());

		return response;
	}

	@Transactional
	public MealResponse update(UUID mealId, UpdateMealRequest dto) {

		// 1. Find existing meal
		Meal meal = mealRepository.findByIdWithItems(mealId)
				.orElseThrow(() ->
						new MealNotFoundException(
								"Meal not found: " + mealId
						)
				);

		// 2. Update basic meal information
		meal.setName(dto.getName());
		meal.setDescription(dto.getDescription());

		// 3. Remove existing items
		meal.getItems().clear();

		// 4. Reset totals
		BigDecimal totalCalories = BigDecimal.ZERO;
		BigDecimal totalProtein = BigDecimal.ZERO;
		BigDecimal totalCarbs = BigDecimal.ZERO;
		BigDecimal totalFat = BigDecimal.ZERO;

		// 5. Add updated items
		for (UpdateMealItemRequest itemDto : dto.getItems()) {

			Food food = foodRepository
					.findByNameIgnoreCase(itemDto.getFoodName())
					.orElseThrow(() ->
							new FoodNotFoundException(
									"Food not found: "
											+ itemDto.getFoodName()
							)
					);

			BigDecimal quantity = itemDto.getQuantity();

			// Calculate multiplier
			BigDecimal multiplier = quantity.divide(
					food.getServingSize(),
					6,
					RoundingMode.HALF_UP
			);

			// Calculate nutrition
			BigDecimal calories =
					food.getCalories().multiply(multiplier);

			BigDecimal protein =
					food.getProteinG().multiply(multiplier);

			BigDecimal carbs =
					food.getCarbsG().multiply(multiplier);

			BigDecimal fat =
					food.getFatG().multiply(multiplier);


			// Create MealItem
			MealItem mealItem = new MealItem();

			mealItem.setMeal(meal);
			mealItem.setFood(food);
			mealItem.setQuantity(quantity);

			// Unit comes from the Food record
			mealItem.setUnit(food.getServingUnit());

			meal.getItems().add(mealItem);

			// Add to totals
			totalCalories = totalCalories.add(calories);
			totalProtein = totalProtein.add(protein);
			totalCarbs = totalCarbs.add(carbs);
			totalFat = totalFat.add(fat);
		}

		// 6. Set updated totals
		meal.setTotalCalories(totalCalories);
		meal.setTotalProteinG(totalProtein);
		meal.setTotalCarbsG(totalCarbs);
		meal.setTotalFatG(totalFat);

		// 7. Save
		Meal updatedMeal = mealRepository.save(meal);

		// 8. Return response
		return toMealResponse(updatedMeal);
	}

	@Transactional
	public void delete(UUID id) {
		if (!mealRepository.existsById(id)) {
			throw new ApiException("Meal not found", org.springframework.http.HttpStatus.NOT_FOUND);
		}
		mealRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Meal requireByName(String mealName) {
		Meal meal = mealRepository.findByNameIgnoreCase(mealName)
			.orElseThrow(() -> new ApiException("Meal not found", org.springframework.http.HttpStatus.NOT_FOUND));
		meal.getItems().size();
		return meal;
	}

	private BigDecimal orZero(BigDecimal value) {
		return value == null ? BigDecimal.ZERO : value;
	}

	private Meal requireMeal(UUID id) {
		return mealRepository.findByIdWithItems(id)
			.orElseThrow(() -> new ApiException("Meal not found", org.springframework.http.HttpStatus.NOT_FOUND));
	}

	private MealSummaryResponse toDto(Meal meal) {
		MealSummaryResponse dto = new MealSummaryResponse();
		dto.setId(meal.getId());
		dto.setName(meal.getName());
		dto.setDescription(meal.getDescription());
		dto.setTotalCalories(meal.getTotalCalories());
		dto.setTotalProteinG(meal.getTotalProteinG());
		dto.setTotalCarbsG(meal.getTotalCarbsG());
		dto.setTotalFatG(meal.getTotalFatG());
		return dto;
	}

    @Transactional
    public MealResponse createFromPayload(AIPayload payload) {

        if (payload.getMeal() == null) {
            throw new ApiException("Meal details are required");
        }

        AIPayload.MealPayload meal = payload.getMeal();

        if (meal.getName() == null || meal.getName().isBlank()) {
            throw new ApiException("Meal name is required");
        }

        if (meal.getItems() == null || meal.getItems().isEmpty()) {
            throw new ApiException("At least one meal item is required");
        }

        CreateMealRequest request = new CreateMealRequest();

        log.info("creating createMealRequest");
        request.setName(meal.getName());
        request.setDescription(meal.getDescription());

        List<CreateMealItemRequest> items = new ArrayList<>();

        for (AIPayload.MealItemPayload item : meal.getItems()) {

            if (item.getFoodName() == null ||
                    item.getFoodName().isBlank()) {
                throw new ApiException("Food name is required");
            }

            if (item.getQuantity() == null ||
                    item.getQuantity().signum() <= 0) {
                throw new ApiException(
                        "Quantity must be greater than 0"
                );
            }

            CreateMealItemRequest itemRequest =
                    new CreateMealItemRequest();

            itemRequest.setFoodName(item.getFoodName());
            itemRequest.setQuantity(item.getQuantity());

            items.add(itemRequest);
        }

        request.setItems(items);

        return create(request);
    }

	public MealResponse updateFromPayload(
			AIPayload payload) {

		if (payload.getMealId() == null) {
			throw new ApiException(
					"mealId is required for UPDATE_MEAL"
			);
		}

		if (payload.getMeal() == null) {
			throw new ApiException(
					"meal is required for UPDATE_MEAL"
			);
		}

		var aiMeal = payload.getMeal();

		UpdateMealRequest request = new UpdateMealRequest();

		request.setName(aiMeal.getName());
		request.setDescription(aiMeal.getDescription());

		List<UpdateMealItemRequest> items =
				aiMeal.getItems()
						.stream()
						.map(item -> {

							UpdateMealItemRequest requestItem =
									new UpdateMealItemRequest();

							requestItem.setFoodName(
									item.getFoodName()
							);

							requestItem.setQuantity(
									item.getQuantity()
							);

							return requestItem;
						})
						.toList();

		request.setItems(items);

		return update(
				payload.getMealId(),
				request
		);
	}

}
