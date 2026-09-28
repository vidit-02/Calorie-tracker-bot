package com.example.calorie.controller;

import java.util.List;
import java.util.UUID;

import com.example.calorie.dto.food.CreateFoodRequest;
import com.example.calorie.dto.food.FoodResponse;
import com.example.calorie.dto.food.UpdateFoodRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.calorie.dto.FoodDto;
import com.example.calorie.service.FoodService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

	private final FoodService foodService;

	public FoodController(FoodService foodService) {
		this.foodService = foodService;
	}

	// Get all foods
	@GetMapping
	public List<FoodResponse> listFoods() {
		return foodService.findAll();
	}

	// Create a new food
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FoodResponse createFood(
			@Valid @RequestBody CreateFoodRequest dto) {

		return foodService.create(dto);
	}

	// Update an existing food
	@PutMapping("/{foodId}")
	public FoodResponse updateFood(
			@PathVariable UUID foodId,
			@Valid @RequestBody UpdateFoodRequest dto) {

		return foodService.update(foodId, dto);
	}

	// Delete an existing food
	@DeleteMapping("/{foodId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFood(@PathVariable UUID foodId) {

		foodService.delete(foodId);
	}

}
