package com.example.calorie.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "meal_items")
public class MealItem {
	@Id @GeneratedValue private UUID id;
	@ManyToOne(optional = false) @JoinColumn(name = "meal_id", nullable = false) private Meal meal;
	@ManyToOne(optional = false) @JoinColumn(name = "food_id", nullable = false) private Food food;
	@Column(nullable = false, precision = 10, scale = 3) private BigDecimal quantity;
	@Column(nullable = false, length = 10) private String unit;
	public UUID getId() { return id; }
	public void setId(UUID value) { id = value; }
	public Meal getMeal() { return meal; }
	public void setMeal(Meal value) { meal = value; }
	public Food getFood() { return food; }
	public void setFood(Food value) { food = value; }
	public BigDecimal getQuantity() { return quantity; }
	public void setQuantity(BigDecimal value) { quantity = value; }
	public String getUnit() { return unit; }
	public void setUnit(String value) { unit = value; }
}
