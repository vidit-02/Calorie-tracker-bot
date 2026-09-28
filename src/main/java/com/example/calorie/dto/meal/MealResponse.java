package com.example.calorie.dto.meal;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class MealResponse {
    private UUID id;

    private String name;

    private String description;

    private List<MealItemResponse> items;

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

    public List<MealItemResponse> getItems() {
        return items;
    }

    public void setItems(List<MealItemResponse> items) {
        this.items = items;
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
