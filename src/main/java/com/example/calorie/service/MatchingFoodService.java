package com.example.calorie.service;

import com.example.calorie.entity.Food;
import com.example.calorie.entity.FoodAlias;
import com.example.calorie.exception.ApiException;
import com.example.calorie.exception.FoodNotFoundException;
import com.example.calorie.repository.FoodAliasRepository;
import com.example.calorie.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchingFoodService {

    private final FoodRepository foodRepository;
    private final FoodAliasRepository foodAliasRepository;

    public MatchingFoodService(
            FoodRepository foodRepository,
            FoodAliasRepository foodAliasRepository) {
        this.foodRepository = foodRepository;
        this.foodAliasRepository = foodAliasRepository;
    }

    public Food findFood(String foodName) {

        if (foodName == null || foodName.isBlank()) {
            throw new ApiException("Food name is required");
        }

        String normalizedName = normalize(foodName);

        // 1. Try canonical food name
        Optional<Food> food =
                foodRepository.findByNameIgnoreCase(normalizedName);

        if (food.isPresent()) {
            return food.get();
        }

        // 2. Try alias
        Optional<FoodAlias> alias =
                foodAliasRepository.findByAliasIgnoreCase(normalizedName);

        if (alias.isPresent()) {
            return alias.get().getFood();
        }

        throw new FoodNotFoundException(
                "Food not found: " + foodName
        );
    }

    private String normalize(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }
}