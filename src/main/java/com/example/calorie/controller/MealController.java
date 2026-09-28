package com.example.calorie.controller;

import java.util.List;
import java.util.UUID;

import com.example.calorie.dto.meal.CreateMealRequest;
import com.example.calorie.dto.meal.MealResponse;
import com.example.calorie.dto.meal.MealSummaryResponse;
import com.example.calorie.dto.meal.UpdateMealRequest;
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

import com.example.calorie.service.MealService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/meals")
public class MealController {

	private final MealService mealService;

	public MealController(MealService mealService) {
		this.mealService = mealService;
	}

	//get the list of all meals will return the summary not the entire food list of items
	@GetMapping
	public List<MealSummaryResponse> listMeals() {
		return mealService.findAll();
	}

	@GetMapping("/{mealId}")
	public MealResponse getMeal(@PathVariable UUID mealId) {
		return mealService.findById(mealId);
	}

	//create a new meal
	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public MealResponse createMeal(@Valid @RequestBody CreateMealRequest dto) {
		return mealService.create(dto);
	}

	// Update a specific existing meal
	@PutMapping("/{mealId}")
	public MealResponse updateMeal(
			@PathVariable UUID mealId,
			@Valid @RequestBody UpdateMealRequest dto) {

		return mealService.update(mealId, dto);
	}

	//delete an existing meal
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMeal(@PathVariable UUID id) {
		mealService.delete(id);
	}

}
