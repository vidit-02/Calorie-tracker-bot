package com.example.calorie.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "user_profile")
public class UserProfile {

	@Id
	@Column(name = "user_id", nullable = false, updatable = false)
	private UUID userId = UUID.randomUUID();

	@Column(length = 50)
	private String name;

	@Column(name = "weight_kg")
	private Double weightKg;

	@Column(name = "daily_calorie_goal")
	private Integer dailyCalorieGoal;

	@Column(name = "daily_protein_goal_g")
	private Double dailyProteinGoalG;

	@Column(name = "daily_carbs_goal_g")
	private Double dailyCarbsGoalG;

	@Column(name = "daily_fat_goal_g")
	private Double dailyFatGoalG;

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getWeightKg() {
		return weightKg;
	}

	public void setWeightKg(Double weightKg) {
		this.weightKg = weightKg;
	}

	public Integer getDailyCalorieGoal() {
		return dailyCalorieGoal;
	}

	public void setDailyCalorieGoal(Integer dailyCalorieGoal) {
		this.dailyCalorieGoal = dailyCalorieGoal;
	}

	public Double getDailyProteinGoalG() {
		return dailyProteinGoalG;
	}

	public void setDailyProteinGoalG(Double dailyProteinGoalG) {
		this.dailyProteinGoalG = dailyProteinGoalG;
	}

	public Double getDailyCarbsGoalG() {
		return dailyCarbsGoalG;
	}

	public void setDailyCarbsGoalG(Double dailyCarbsGoalG) {
		this.dailyCarbsGoalG = dailyCarbsGoalG;
	}

	public Double getDailyFatGoalG() {
		return dailyFatGoalG;
	}

	public void setDailyFatGoalG(Double dailyFatGoalG) {
		this.dailyFatGoalG = dailyFatGoalG;
	}


}
