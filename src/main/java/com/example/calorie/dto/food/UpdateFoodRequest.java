package com.example.calorie.dto.food;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class UpdateFoodRequest {

    @NotBlank
    private String name;

    private String brand;

    private String category;

    @NotNull
    @Positive
    @JsonProperty("serving_size")
    private BigDecimal servingSize;

    @NotBlank
    @JsonProperty("serving_unit")
    private String servingUnit;

    @NotNull
    @PositiveOrZero
    private BigDecimal calories;

    @NotNull
    @PositiveOrZero
    @JsonProperty("protein_g")
    private BigDecimal proteinG;

    @NotNull
    @PositiveOrZero
    @JsonProperty("carbs_g")
    private BigDecimal carbsG;

    @NotNull
    @PositiveOrZero
    @JsonProperty("fat_g")
    private BigDecimal fatG;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public @NotNull @Positive BigDecimal getServingSize() {
        return servingSize;
    }

    public void setServingSize(@NotNull @Positive BigDecimal servingSize) {
        this.servingSize = servingSize;
    }

    public @NotBlank String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(@NotBlank String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public @NotNull @PositiveOrZero BigDecimal getCalories() {
        return calories;
    }

    public void setCalories(@NotNull @PositiveOrZero BigDecimal calories) {
        this.calories = calories;
    }

    public @NotNull @PositiveOrZero BigDecimal getProteinG() {
        return proteinG;
    }

    public void setProteinG(@NotNull @PositiveOrZero BigDecimal proteinG) {
        this.proteinG = proteinG;
    }

    public @NotNull @PositiveOrZero BigDecimal getCarbsG() {
        return carbsG;
    }

    public void setCarbsG(@NotNull @PositiveOrZero BigDecimal carbsG) {
        this.carbsG = carbsG;
    }

    public @NotNull @PositiveOrZero BigDecimal getFatG() {
        return fatG;
    }

    public void setFatG(@NotNull @PositiveOrZero BigDecimal fatG) {
        this.fatG = fatG;
    }

    // getters and setters
}