package com.example.calorie.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfileDto {

	@JsonProperty("user_id")
	private UUID userId;

	private String name;

	@JsonProperty("weight_kg")
	private Double weightKg;

	@JsonProperty("daily_calorie_goal")
	private Integer dailyCalorieGoal;

	@JsonProperty("daily_protein_goal_g")
	private Double dailyProteinGoalG;

	@JsonProperty("daily_carbs_goal_g")
	private Double dailyCarbsGoalG;

	@JsonProperty("daily_fat_goal_g")
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
