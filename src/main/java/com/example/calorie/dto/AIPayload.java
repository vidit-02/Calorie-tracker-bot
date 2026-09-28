package com.example.calorie.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.calorie.config.MealType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AIPayload {

	private String intent;

	// Clarification
	private Boolean needsClarification;

	private String clarification;

	private List<Item> items;

	private String mealName;

	private MealPayload meal;

	private FoodPayload food;

	private UUID foodId;

	private UUID mealId;

	private UUID entryId;

	private MealType mealType;

	private Map<String, Object> fields;

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public MealPayload getMeal() {
		return meal;
	}

	public void setMeal(MealPayload meal) {
		this.meal = meal;
	}

	public FoodPayload getFood() {
		return food;
	}

	public void setFood(FoodPayload food) {
		this.food = food;
	}

	public UUID getFoodId() {
		return foodId;
	}

	public void setFoodId(UUID foodId) {
		this.foodId = foodId;
	}

	public UUID getMealId() {
		return mealId;
	}

	public void setMealId(UUID mealId) {
		this.mealId = mealId;
	}

	public UUID getEntryId() {
		return entryId;
	}

	public void setEntryId(UUID entryId) {
		this.entryId = entryId;
	}

	public MealType getMealType() {
		return mealType;
	}

	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}

	public Boolean getNeedsClarification() {
		return needsClarification;
	}

	public void setNeedsClarification(Boolean needsClarification) {
		this.needsClarification = needsClarification;
	}

	public String getClarification() {
		return clarification;
	}

	public void setClarification(String clarification) {
		this.clarification = clarification;
	}

	// getters/setters


	public static class Item {
		private String foodName;
		private BigDecimal quantity;
		private String unit;

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

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		// getters/setters
	}


	public static class FoodPayload {
		private String name;
		private String brand;
		private String category;
		private BigDecimal servingSize;
		private String servingUnit;
		private BigDecimal calories;
		private BigDecimal proteinG;
		private BigDecimal carbsG;
		private BigDecimal fatG;

		public String getName() {
			return name;
		}

		public void setName(String name) {
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

		public BigDecimal getServingSize() {
			return servingSize;
		}

		public void setServingSize(BigDecimal servingSize) {
			this.servingSize = servingSize;
		}

		public String getServingUnit() {
			return servingUnit;
		}

		public void setServingUnit(String servingUnit) {
			this.servingUnit = servingUnit;
		}

		public BigDecimal getCalories() {
			return calories;
		}

		public void setCalories(BigDecimal calories) {
			this.calories = calories;
		}

		public BigDecimal getProteinG() {
			return proteinG;
		}

		public void setProteinG(BigDecimal proteinG) {
			this.proteinG = proteinG;
		}

		public BigDecimal getCarbsG() {
			return carbsG;
		}

		public void setCarbsG(BigDecimal carbsG) {
			this.carbsG = carbsG;
		}

		public BigDecimal getFatG() {
			return fatG;
		}

		public void setFatG(BigDecimal fatG) {
			this.fatG = fatG;
		}

		// getters/setters
	}


	public static class MealPayload {
		private String name;
		private String description;
		private List<MealItemPayload> items;

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

		public List<MealItemPayload> getItems() {
			return items;
		}

		public void setItems(List<MealItemPayload> items) {
			this.items = items;
		}

		// getters/setters
	}


	public static class MealItemPayload {
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


		// getters/setters
	}
}