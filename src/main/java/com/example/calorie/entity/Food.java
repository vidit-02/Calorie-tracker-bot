package com.example.calorie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "food")
public class Food {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(unique = true, nullable = false, length = 100)
	private String name;

	@Column(length = 50)
	private String brand;

	@Column(length = 100)
	private String category;

	@Column(name = "serving_size", nullable = false, precision = 10, scale = 3)
	private BigDecimal servingSize;

	@Column(name = "serving_unit", nullable = false, length = 10)
	private String servingUnit;

	private BigDecimal calories;

	@Column(name = "protein_g")
	private BigDecimal proteinG;

	@Column(name = "carbs_g")
	private BigDecimal carbsG;

	@Column(name = "fat_g")
	private BigDecimal fatG;

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

}
