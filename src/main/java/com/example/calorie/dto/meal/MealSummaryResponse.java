package com.example.calorie.dto.meal;

import java.math.BigDecimal;
import java.util.UUID;

public class MealSummaryResponse {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal totalCalories;

    private BigDecimal totalProteinG;

    private BigDecimal totalCarbsG;

    private BigDecimal totalFatG;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(BigDecimal totalCalories) {
        this.totalCalories = totalCalories;
    }

    public BigDecimal getTotalProteinG() {
        return totalProteinG;
    }

    public void setTotalProteinG(BigDecimal totalProteinG) {
        this.totalProteinG = totalProteinG;
    }

    public BigDecimal getTotalCarbsG() {
        return totalCarbsG;
    }

    public void setTotalCarbsG(BigDecimal totalCarbsG) {
        this.totalCarbsG = totalCarbsG;
    }

    public BigDecimal getTotalFatG() {
        return totalFatG;
    }

    public void setTotalFatG(BigDecimal totalFatG) {
        this.totalFatG = totalFatG;
    }

}
/*
example response
[
  {
    "id": "7f3a9c21-5d84-4b67-9e12-8c4f6a2d7310",
    "name": "My Breakfast",
    "description": "Regular breakfast",
    "totalCalories": 624.50,
    "totalProteinG": 39.20,
    "totalCarbsG": 25.40,
    "totalFatG": 38.10
  },
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "My Dinner",
    "description": "Regular dinner",
    "totalCalories": 720.00,
    "totalProteinG": 42.00,
    "totalCarbsG": 65.00,
    "totalFatG": 25.00
  }
]
 */