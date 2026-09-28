package com.example.calorie.dto.meal;

import java.math.BigDecimal;

public class UpdateMealItemRequest {
    private String foodName;

    private BigDecimal quantity;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
