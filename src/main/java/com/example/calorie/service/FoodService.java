package com.example.calorie.service;

import java.util.List;
import java.util.UUID;

import com.example.calorie.dto.AIPayload;
import com.example.calorie.dto.food.CreateFoodRequest;
import com.example.calorie.dto.food.FoodResponse;
import com.example.calorie.dto.food.UpdateFoodRequest;
import com.example.calorie.exception.FoodNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.calorie.dto.FoodDto;
import com.example.calorie.entity.Food;
import com.example.calorie.exception.ApiException;
import com.example.calorie.repository.FoodRepository;

@Service
public class FoodService {

	private final FoodRepository foodRepository;

	public FoodService(FoodRepository foodRepository) {
		this.foodRepository = foodRepository;
	}

	public List<FoodResponse> findAll() {

		return foodRepository.findAll()
				.stream()
				.map(this::toFoodResponse)
				.toList();
	}

	public FoodDto findById(UUID id) {
		return toDto(requireFood(id));
	}

	@Transactional
	public FoodResponse create(CreateFoodRequest dto) {

		Food food = new Food();

		food.setName(dto.getName());
		food.setBrand(dto.getBrand());
		food.setCategory(dto.getCategory());

		food.setServingSize(dto.getServingSize());
		food.setServingUnit(dto.getServingUnit());

		food.setCalories(dto.getCalories());
		food.setProteinG(dto.getProteinG());
		food.setCarbsG(dto.getCarbsG());
		food.setFatG(dto.getFatG());


		Food savedFood = foodRepository.save(food);

		return toFoodResponse(savedFood);
	}

	@Transactional
	public FoodResponse update(UUID foodId, UpdateFoodRequest dto) {

		Food food = foodRepository.findById(foodId)
				.orElseThrow(() ->
						new FoodNotFoundException(
								"Food not found: " + foodId
						)
				);

		food.setName(dto.getName());
		food.setBrand(dto.getBrand());
		food.setCategory(dto.getCategory());

		food.setServingSize(dto.getServingSize());
		food.setServingUnit(dto.getServingUnit());

		food.setCalories(dto.getCalories());
		food.setProteinG(dto.getProteinG());
		food.setCarbsG(dto.getCarbsG());
		food.setFatG(dto.getFatG());

		Food updatedFood = foodRepository.save(food);

		return toFoodResponse(updatedFood);
	}

	private FoodResponse toFoodResponse(Food food) {

		FoodResponse response = new FoodResponse();

		response.setId(food.getId());
		response.setName(food.getName());
		response.setBrand(food.getBrand());
		response.setCategory(food.getCategory());

		response.setServingSize(food.getServingSize());
		response.setServingUnit(food.getServingUnit());

		response.setCalories(food.getCalories());
		response.setProteinG(food.getProteinG());
		response.setCarbsG(food.getCarbsG());
		response.setFatG(food.getFatG());

		return response;
	}

	@Transactional
	public void delete(UUID foodId) {

		Food food = foodRepository.findById(foodId)
				.orElseThrow(() ->
						new FoodNotFoundException(
								"Food not found: " + foodId
						)
				);

		foodRepository.delete(food);
	}

	@Transactional
	public FoodResponse createFromPayload(
			AIPayload.FoodPayload foodPayload) {

		validateFoodPayload(foodPayload,false);

		CreateFoodRequest request = new CreateFoodRequest();

		request.setName(foodPayload.getName());
		request.setBrand(foodPayload.getBrand());
		request.setCategory(foodPayload.getCategory());

		request.setServingSize(
				foodPayload.getServingSize()
		);

		request.setServingUnit(
				foodPayload.getServingUnit()
		);

		request.setCalories(
				foodPayload.getCalories()
		);

		request.setProteinG(
				foodPayload.getProteinG()
		);

		request.setCarbsG(
				foodPayload.getCarbsG()
		);

		request.setFatG(
				foodPayload.getFatG()
		);

		return create(request);
	}

	public FoodResponse updateFromPayload(
			AIPayload payload) {

		if (payload.getFoodId() == null) {
			throw new ApiException(
					"foodId is required for UPDATE_FOOD"
			);
		}

		validateFoodPayload(payload.getFood(),true);

		AIPayload.FoodPayload food = payload.getFood();

		Food existingFood = foodRepository.findById(payload.getFoodId())
				.orElseThrow(() ->
						new FoodNotFoundException(
								"Food not found: " + payload.getFoodId()
						)
				);

		UpdateFoodRequest request = new UpdateFoodRequest();

		request.setName(
				food.getName() != null
						? food.getName()
						: existingFood.getName()
		);

		request.setBrand(
				food.getBrand() != null
						? food.getBrand()
						: existingFood.getBrand()
		);

		request.setCategory(
				food.getCategory() != null
						? food.getCategory()
						: existingFood.getCategory()
		);

		request.setServingSize(
				food.getServingSize() != null
						? food.getServingSize()
						: existingFood.getServingSize()
		);

		request.setServingUnit(
				food.getServingUnit() != null
						? food.getServingUnit()
						: existingFood.getServingUnit()
		);

		request.setCalories(
				food.getCalories() != null
						? food.getCalories()
						: existingFood.getCalories()
		);

		request.setProteinG(
				food.getProteinG() != null
						? food.getProteinG()
						: existingFood.getProteinG()
		);

		request.setCarbsG(
				food.getCarbsG() != null
						? food.getCarbsG()
						: existingFood.getCarbsG()
		);

		request.setFatG(
				food.getFatG() != null
						? food.getFatG()
						: existingFood.getFatG()
		);


		return update(
				payload.getFoodId(),
				request
		);
	}

	private Food requireFood(UUID id) {
		return foodRepository.findById(id)
			.orElseThrow(() -> new ApiException("Food not found", org.springframework.http.HttpStatus.NOT_FOUND));
	}

	private void validateFoodPayload(
			AIPayload.FoodPayload food,
			boolean partialUpdate) {

		if (food == null) {
			throw new ApiException("Food details are required");
		}

		if (food.getName() == null || food.getName().isBlank()) {
			throw new ApiException("Food name is required");
		}

		if (food.getServingSize() != null &&
				food.getServingSize().signum() <= 0) {

			throw new ApiException(
					"Serving size must be greater than 0"
			);
		}

		if (!food.getServingUnit().equals("g") &&
				!food.getServingUnit().equals("ml") &&
				!food.getServingUnit().equals("piece") &&
				!food.getServingUnit().equals("slice")) {

			throw new ApiException(
					"Serving unit must be one of: g, ml, piece, slice"
			);
		}

		if (food.getCalories() != null &&
				food.getCalories().signum() < 0) {

			throw new ApiException(
					"Calories must be zero or greater"
			);
		}

		if (food.getProteinG() != null &&
				food.getProteinG().signum() < 0) {

			throw new ApiException(
					"Protein must be zero or greater"
			);
		}

		if (food.getCarbsG() != null &&
				food.getCarbsG().signum() < 0) {

			throw new ApiException(
					"Carbohydrates must be zero or greater"
			);
		}

		if (food.getFatG() != null &&
				food.getFatG().signum() < 0) {

			throw new ApiException(
					"Fat must be zero or greater"
			);
		}

		if (!partialUpdate) {
			if (food.getServingSize() == null) {
				throw new ApiException("Serving size is required");
			}

			if (food.getServingUnit() == null ||
					food.getServingUnit().isBlank()) {
				throw new ApiException("Serving unit is required");
			}

			if (food.getCalories() == null) {
				throw new ApiException("Calories are required");
			}

			if (food.getProteinG() == null) {
				throw new ApiException("Protein is required");
			}

			if (food.getCarbsG() == null) {
				throw new ApiException("Carbohydrates are required");
			}

			if (food.getFatG() == null) {
				throw new ApiException("Fat is required");
			}
		}
	}

//	private Food fromDto(Food food, FoodDto dto) {
//		food.setName(dto.getName());
//		food.setBrand(dto.getBrand());
//		food.setCategory(dto.getCategory());
//		food.setServingSize(dto.getServingSize());
//		food.setServingUnit(dto.getServingUnit());
//		food.setCalories(dto.getCalories());
//		food.setProteinG(dto.getProteinG());
//		food.setCarbsG(dto.getCarbsG());
//		food.setFatG(dto.getFatG());
//		return food;
//	}

	private FoodDto toDto(Food food) {
		FoodDto dto = new FoodDto();
		dto.setId(food.getId());
		dto.setName(food.getName());
		dto.setBrand(food.getBrand());
		dto.setCategory(food.getCategory());
		dto.setServingSize(food.getServingSize());
		dto.setServingUnit(food.getServingUnit());
		dto.setCalories(food.getCalories());
		dto.setProteinG(food.getProteinG());
		dto.setCarbsG(food.getCarbsG());
		dto.setFatG(food.getFatG());
		return dto;
	}

}
