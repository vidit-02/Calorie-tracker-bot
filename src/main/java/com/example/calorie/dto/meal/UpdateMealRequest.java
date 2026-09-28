package com.example.calorie.dto.meal;

import java.util.List;

public class UpdateMealRequest {
    private String name;

    private String description;

    private List<UpdateMealItemRequest> items;

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

    public List<UpdateMealItemRequest> getItems() {
        return items;
    }

    public void setItems(List<UpdateMealItemRequest> items) {
        this.items = items;
    }
}
