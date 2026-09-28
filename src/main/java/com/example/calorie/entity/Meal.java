package com.example.calorie.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "meals")
public class Meal {
	@Id @GeneratedValue private UUID id;
	@Column(unique = true, nullable = false, length = 255) private String name;
	@Column(columnDefinition = "TEXT") private String description;
	@Column(name = "total_calories", nullable = false, precision = 10, scale = 2) private BigDecimal totalCalories = BigDecimal.ZERO;
	@Column(name = "total_protein_g", nullable = false, precision = 10, scale = 2) private BigDecimal totalProteinG = BigDecimal.ZERO;
	@Column(name = "total_carbs_g", nullable = false, precision = 10, scale = 2) private BigDecimal totalCarbsG = BigDecimal.ZERO;
	@Column(name = "total_fat_g", nullable = false, precision = 10, scale = 2) private BigDecimal totalFatG = BigDecimal.ZERO;
	@OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true) private List<MealItem> items = new ArrayList<>();
	public UUID getId() { return id; }
	public void setId(UUID value) { id = value; }
	public String getName() { return name; }
	public void setName(String value) { name = value; }
	public String getDescription() { return description; }
	public void setDescription(String value) { description = value; }
	public BigDecimal getTotalCalories() { return totalCalories; }
	public void setTotalCalories(BigDecimal value) { totalCalories = value; }
	public BigDecimal getTotalProteinG() { return totalProteinG; }
	public void setTotalProteinG(BigDecimal value) { totalProteinG = value; }
	public BigDecimal getTotalCarbsG() { return totalCarbsG; }
	public void setTotalCarbsG(BigDecimal value) { totalCarbsG = value; }
	public BigDecimal getTotalFatG() { return totalFatG; }
	public void setTotalFatG(BigDecimal value) { totalFatG = value; }
	public List<MealItem> getItems() { return items; }
	public void setItems(List<MealItem> value) { items = value; }
}
