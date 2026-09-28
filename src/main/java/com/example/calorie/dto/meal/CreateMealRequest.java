package com.example.calorie.dto.meal;

import java.util.List;

public class CreateMealRequest {
    private String name;

    private String description;

    private List<CreateMealItemRequest> items;

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

    public List<CreateMealItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CreateMealItemRequest> items) {
        this.items = items;
    }
}

//
// request example
//  {
//        "name": "My Breakfast",
//        "description": "My regular breakfast",
//        "items": [
//        {
//        "foodName": "Amul Paneer",
//        "quantity": 150
//        },
//        {
//        "foodName": "Amul Toned Milk",
//        "quantity": 300
//        }
//        ]
//        }
