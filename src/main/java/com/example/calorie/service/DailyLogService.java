package com.example.calorie.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import com.example.calorie.config.EntryType;
import com.example.calorie.config.MealType;
import com.example.calorie.dto.AIPayload;
import com.example.calorie.dto.dailyLog.DailyLogEntry;
import com.example.calorie.dto.dailyLog.DailyLogEntryResponse;
import com.example.calorie.exception.*;
import com.example.calorie.repository.FoodRepository;
import com.example.calorie.repository.MealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.calorie.dto.dailyLog.DailyLogDto;
import com.example.calorie.entity.DailyLog;
import com.example.calorie.entity.Food;
import com.example.calorie.entity.Meal;
import com.example.calorie.repository.DailyLogRepository;

@Service
public class DailyLogService {

	private static final Logger log = LoggerFactory.getLogger(DailyLogService.class);

	private final DailyLogRepository dailyLogRepository;
	private final FoodRepository foodRepository;
	private final MealRepository mealRepository;
	private final MatchingFoodService matchingFoodService;

	public DailyLogService(
			DailyLogRepository dailyLogRepository,
			FoodRepository foodRepository,
			MealRepository mealRepository,
			MatchingFoodService matchingFoodService) {

		this.dailyLogRepository = dailyLogRepository;
		this.foodRepository = foodRepository;
		this.mealRepository = mealRepository;
		this.matchingFoodService = matchingFoodService;
	}

	@Transactional(readOnly = true)
	public DailyLogDto getDailyLog(LocalDate date) {

		DailyLog dailyLog = dailyLogRepository
				.findByLogDate(date)
				.orElse(null);

		// No log for this date
		if (dailyLog == null) {
			return createEmptyDailyLogResponse(date);
		}

		return toDailyLogResponse(dailyLog);
	}

	private DailyLogDto createEmptyDailyLogResponse(
			LocalDate date) {

		DailyLogDto response = new DailyLogDto();

		response.setLogDate(date);
		response.setTotalCalories(BigDecimal.ZERO);
		response.setTotalProteinG(BigDecimal.ZERO);
		response.setTotalCarbsG(BigDecimal.ZERO);
		response.setTotalFatG(BigDecimal.ZERO);
		response.setEntries(new ArrayList<>());

		return response;
	}

	private DailyLogDto toDailyLogResponse(DailyLog dailyLog) {

		DailyLogDto response = new DailyLogDto();

		response.setId(dailyLog.getId());
		response.setLogDate(dailyLog.getLogDate());

		response.setTotalCalories(dailyLog.getTotalCalories());
		response.setTotalProteinG(dailyLog.getTotalProteinG());
		response.setTotalCarbsG(dailyLog.getTotalCarbsG());
		response.setTotalFatG(dailyLog.getTotalFatG());

		List<DailyLogEntryResponse> entries =
				dailyLog.getEntries()
						.stream()
						.map(this::toEntryResponse)
						.toList();

		response.setEntries(entries);

		return response;
	}

	private DailyLogEntryResponse toEntryResponse(
			DailyLogEntry entry) {

		DailyLogEntryResponse response =
				new DailyLogEntryResponse();

		response.setEntryId(entry.getEntryId());
		response.setType(entry.getType());

		response.setFoodId(entry.getFoodId());
		response.setFoodName(entry.getFoodName());

		response.setMealId(entry.getMealId());
		response.setMealName(entry.getMealName());

		response.setQuantity(entry.getQuantity());
		response.setCalories(entry.getCalories());
		response.setMealType(entry.getMealType());

		return response;
	}

	@Transactional
	public DailyLogDto logFood(
			String foodName,
			BigDecimal quantity,
			MealType mealType,
			LocalDate date) {

		Food food = foodRepository
				.findByNameIgnoreCase(foodName)
				.orElseThrow(() ->
						new FoodNotFoundException(
								"Food not found: " + foodName
						)
				);

		BigDecimal multiplier = quantity.divide(
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


		DailyLog dailyLog = getOrCreateDailyLog(date);

		DailyLogEntry entry = new DailyLogEntry();

		entry.setEntryId(UUID.randomUUID());
		entry.setType(EntryType.FOOD);

		entry.setFoodId(food.getId());
		entry.setFoodName(food.getName());

		entry.setQuantity(quantity);
		entry.setCalories(calories);
		entry.setMealType(mealType);

		dailyLog.getEntries().add(entry);

		addNutritionToDailyLog(
				dailyLog,
				calories,
				protein,
				carbs,
				fat
		);

		DailyLog savedLog =
				dailyLogRepository.save(dailyLog);

		return toDailyLogResponse(savedLog);
	}

	@Transactional
	public DailyLogDto logMeal(
			String mealName,
			MealType mealType,
			LocalDate date) {

		Meal meal = mealRepository
				.findByNameIgnoreCase(mealName)
				.orElseThrow(() ->
						new MealNotFoundException(
								"Meal not found: " + mealName
						)
				);

		DailyLog dailyLog =
				getOrCreateDailyLog(date);

		DailyLogEntry entry = new DailyLogEntry();

		entry.setEntryId(UUID.randomUUID());
		entry.setType(EntryType.MEAL);

		entry.setMealId(meal.getId());
		entry.setMealName(meal.getName());

		entry.setQuantity(BigDecimal.ONE);
		entry.setCalories(meal.getTotalCalories());
		entry.setMealType(mealType);

		dailyLog.getEntries().add(entry);

		addNutritionToDailyLog(
				dailyLog,
				meal.getTotalCalories(),
				meal.getTotalProteinG(),
				meal.getTotalCarbsG(),
				meal.getTotalFatG()
		);

		DailyLog savedLog =
				dailyLogRepository.save(dailyLog);

		return toDailyLogResponse(savedLog);
	}

	private DailyLog getOrCreateDailyLog(LocalDate date) {

		log.info("Before findByLogDate: {}", date);
		Optional<DailyLog> result =
				dailyLogRepository.findByLogDate(date);

		log.info("After findByLogDate. Found: {}", result.isPresent());

		return result.orElseGet(() -> {

			log.info("Creating new DailyLog for {}", date);

			DailyLog dailyLog = new DailyLog();

			dailyLog.setLogDate(date);
			dailyLog.setTotalCalories(BigDecimal.ZERO);
			dailyLog.setTotalProteinG(BigDecimal.ZERO);
			dailyLog.setTotalCarbsG(BigDecimal.ZERO);
			dailyLog.setTotalFatG(BigDecimal.ZERO);
			dailyLog.setEntries(new ArrayList<>());

			log.info("New DailyLog created");

			return dailyLog;
		});
	}

	private void addNutritionToDailyLog(
			DailyLog dailyLog,
			BigDecimal calories,
			BigDecimal protein,
			BigDecimal carbs,
			BigDecimal fat) {

		dailyLog.setTotalCalories(
				dailyLog.getTotalCalories().add(calories)
		);

		dailyLog.setTotalProteinG(
				dailyLog.getTotalProteinG().add(protein)
		);

		dailyLog.setTotalCarbsG(
				dailyLog.getTotalCarbsG().add(carbs)
		);

		dailyLog.setTotalFatG(
				dailyLog.getTotalFatG().add(fat)
		);

	}

	@Transactional
	public void removeLogEntry(UUID entryId) {

		DailyLog dailyLog = dailyLogRepository
				.findByLogDate(LocalDate.now())
				.orElseThrow(() ->
						new DailyLogNotFoundException(
								"Daily log not found"
						)
				);

		DailyLogEntry entry = dailyLog.getEntries()
				.stream()
				.filter(e -> e.getEntryId().equals(entryId))
				.findFirst()
				.orElseThrow(() ->
						new LogEntryNotFoundException(
								"Log entry not found: " + entryId
						)
				);

		dailyLog.getEntries().remove(entry);

		recalculateDailyTotals(dailyLog);

		dailyLogRepository.save(dailyLog);
	}

	private void recalculateDailyTotals(DailyLog dailyLog) {

		BigDecimal totalCalories = BigDecimal.ZERO;
		BigDecimal totalProtein = BigDecimal.ZERO;
		BigDecimal totalCarbs = BigDecimal.ZERO;
		BigDecimal totalFat = BigDecimal.ZERO;
		BigDecimal totalFiber = BigDecimal.ZERO;

		for (DailyLogEntry entry : dailyLog.getEntries()) {

			totalCalories = totalCalories.add(entry.getCalories());
			totalProtein = totalProtein.add(entry.getProteinG());
			totalCarbs = totalCarbs.add(entry.getCarbsG());
			totalFat = totalFat.add(entry.getFatG());
		}

		dailyLog.setTotalCalories(totalCalories);
		dailyLog.setTotalProteinG(totalProtein);
		dailyLog.setTotalCarbsG(totalCarbs);
		dailyLog.setTotalFatG(totalFat);
	}

	public DailyLogDto logMealFromPayload(
			AIPayload payload) {

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

		return logMeal(
				payload.getMealName(),
				payload.getMealType(),
				LocalDate.now()
		);
	}

	@Transactional
	public DailyLogDto logFoodFromPayload(AIPayload payload) {

		if (payload.getItems() == null || payload.getItems().isEmpty()) {
			throw new ApiException("items are required for LOG_FOOD");
		}

		if (payload.getMealType() == null) {
			throw new ApiException("mealType is required for LOG_FOOD");
		}

		log.info("Starting logFoodFromPayload");

		log.info("Getting or creating daily log...");
		DailyLog dailyLog = getOrCreateDailyLog(LocalDate.now());
		log.info("Daily log obtained: {}", dailyLog.getId());

		for (AIPayload.Item item : payload.getItems()) {

			log.info(
					"Processing food: {}, quantity: {}, unit: {}",
					item.getFoodName(),
					item.getQuantity(),
					item.getUnit()
			);

			if (item.getFoodName() == null || item.getFoodName().isBlank()) {
				throw new ApiException("foodName is required");
			}

			if (item.getQuantity() == null || item.getQuantity().signum() <= 0) {
				throw new ApiException("quantity must be greater than 0");
			}

			log.info("Looking up food: {}", item.getFoodName());

			Food food = matchingFoodService.findFood(item.getFoodName());

			log.info("Food found: {} ({})", food.getName(), food.getId());

			BigDecimal multiplier = item.getQuantity()
					.divide(
							food.getServingSize(),
							6,
							RoundingMode.HALF_UP
					);

			log.info("Multiplier calculated: {}", multiplier);

			BigDecimal calories =
					food.getCalories().multiply(multiplier);

			BigDecimal protein =
					food.getProteinG().multiply(multiplier);

			BigDecimal carbs =
					food.getCarbsG().multiply(multiplier);

			BigDecimal fat =
					food.getFatG().multiply(multiplier);

			DailyLogEntry entry = new DailyLogEntry();

			entry.setEntryId(UUID.randomUUID());
			entry.setType(EntryType.FOOD);
			entry.setFoodId(food.getId());
			entry.setFoodName(food.getName());
			entry.setQuantity(item.getQuantity());

			entry.setCalories(calories);
			entry.setProteinG(protein);
			entry.setCarbsG(carbs);
			entry.setFatG(fat);

			entry.setMealType(payload.getMealType());

			dailyLog.getEntries().add(entry);

			log.info("Entry added to daily log");
		}

		log.info("Recalculating daily totals...");

		recalculateDailyTotals(dailyLog);

		log.info("Daily totals recalculated");

		log.info("Saving daily log...");

		DailyLog savedLog =
				dailyLogRepository.save(dailyLog);

		log.info("Daily log saved: {}", savedLog.getId());

		log.info("Building daily log response...");

		DailyLogDto response = toDailyLogResponse(savedLog);

		log.info("Finished logFoodFromPayload");

		return response;
	}

//	public List<DailyLogDto> getLogs(LocalDate date) {
//		UserProfile user = userService.getOrCreateProfileEntity();
//		return dailyLogRepository.findByUserProfileUserIdAndLogDate(user.getUserId(), date).stream().map(this::toDto).toList();
//	}
//
//	@Transactional
//	public void delete(UUID id) {
//		if (!dailyLogRepository.existsById(id)) {
//			throw new ApiException("Daily log not found", HttpStatus.NOT_FOUND);
//		}
//		dailyLogRepository.deleteById(id);
//	}
//
//	@Transactional
//	public void logFoodFromPayload(AIPayload payload) {
//		if (payload.getItems() == null || payload.getItems().isEmpty()) throw new ApiException("No food items to log");
//		validateMealType(payload.getMealType());
//		DailyLog log = getOrCreateTodayLog();
//		for (AIPayload.Item item : payload.getItems()) {
//			if (item.getQuantity() == null || item.getQuantity() < 0) throw new ApiException("Quantity must be non-negative");
//			Food food = nutritionCalculationService.requireFood(item.getFoodName());
//			int calories = nutritionCalculationService.calculateCalories(food.getName(), item.getQuantity(), item.getUnit());
//			log.getEntries().add(foodEntry(food, item.getQuantity(), calories, payload.getMealType()));
//			log.setTotalCalories(log.getTotalCalories().add(BigDecimal.valueOf(calories)));
//			log.setTotalProteinG(log.getTotalProteinG().add(scaled(food.getProteinG(), food, item.getQuantity())));
//			log.setTotalCarbsG(log.getTotalCarbsG().add(scaled(food.getCarbsG(), food, item.getQuantity())));
//			log.setTotalFatG(log.getTotalFatG().add(scaled(food.getFatG(), food, item.getQuantity())));
//		}
//		dailyLogRepository.save(log);
//	}
//
//	@Transactional
//	public void logMealFromPayload(AIPayload payload) {
//		validateMealType(payload.getMealType());
//		if (payload.getMealName() == null || payload.getMealName().isBlank()) throw new ApiException("meal_name is required");
//		Meal meal = mealService.requireByName(payload.getMealName());
//		DailyLog log = getOrCreateTodayLog();
//		log.getEntries().add(mealEntry(meal, payload.getMealType()));
//		log.setTotalCalories(log.getTotalCalories().add(meal.getTotalCalories()));
//		log.setTotalProteinG(log.getTotalProteinG().add(meal.getTotalProteinG()));
//		log.setTotalCarbsG(log.getTotalCarbsG().add(meal.getTotalCarbsG()));
//		log.setTotalFatG(log.getTotalFatG().add(meal.getTotalFatG()));
//		dailyLogRepository.save(log);
//	}
//
//	private DailyLog getOrCreateTodayLog() {
//		UserProfile user = userService.getOrCreateProfileEntity();
//		return dailyLogRepository.findFirstByUserProfileUserIdAndLogDate(user.getUserId(), LocalDate.now()).orElseGet(() -> {
//			DailyLog log = new DailyLog();
//			log.setUserProfile(user);
//			log.setLogDate(LocalDate.now());
//			return log;
//		});
//	}
//
//	private Map<String, Object> foodEntry(Food food, double quantity, int calories, String mealType) {
//		Map<String, Object> entry = new LinkedHashMap<>();
//		entry.put("type", "FOOD"); entry.put("food_id", food.getId().toString()); entry.put("food_name", food.getName());
//		entry.put("quantity", quantity); entry.put("calories", calories); entry.put("meal_type", mealType);
//		return entry;
//	}
//
//	private Map<String, Object> mealEntry(Meal meal, String mealType) {
//		Map<String, Object> entry = new LinkedHashMap<>();
//		entry.put("type", "MEAL"); entry.put("meal_id", meal.getId().toString()); entry.put("meal_name", meal.getName());
//		entry.put("quantity", 1); entry.put("calories", meal.getTotalCalories()); entry.put("meal_type", mealType);
//		return entry;
//	}
//
//	private BigDecimal scaled(Double nutrient, Food food, double quantity) {
//		if (nutrient == null || food.getServingSize() <= 0) return BigDecimal.ZERO;
//		return BigDecimal.valueOf(nutrient).multiply(BigDecimal.valueOf(quantity))
//			.divide(BigDecimal.valueOf(food.getServingSize()), 2, RoundingMode.HALF_UP);
//	}
//
//	private void validateMealType(String mealType) {
//		if (mealType == null || !MEAL_TYPES.contains(mealType)) {
//			throw new ApiException("meal_type must be one of: BREAKFAST, LUNCH, EVENING_SNACK, DINNER");
//		}
//	}
//
//	private DailyLogDto toDto(DailyLog log) {
//		DailyLogDto dto = new DailyLogDto();
//		dto.setId(log.getId()); dto.setUserId(log.getUserProfile().getUserId()); dto.setLogDate(log.getLogDate());
//		dto.setTotalCalories(log.getTotalCalories()); dto.setTotalProteinG(log.getTotalProteinG());
//		dto.setTotalCarbsG(log.getTotalCarbsG()); dto.setTotalFatG(log.getTotalFatG());
//		dto.setEntries(log.getEntries());
//		return dto;
//	}
}
