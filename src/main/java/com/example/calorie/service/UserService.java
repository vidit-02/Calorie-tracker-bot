package com.example.calorie.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.calorie.dto.UserProfileDto;
import com.example.calorie.entity.UserProfile;
import com.example.calorie.repository.UserProfileRepository;

@Service
public class UserService {

	private final UserProfileRepository userProfileRepository;

	public UserService(UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
	}

	public UserProfileDto getProfile() {
		return toDto(getOrCreateProfileEntity());
	}

	@Transactional
	public UserProfileDto updateProfile(UserProfileDto dto) {
		UserProfile profile = getOrCreateProfileEntity();
		if (dto.getName() != null) {
			profile.setName(dto.getName());
		}
		if (dto.getWeightKg() != null) {
			profile.setWeightKg(dto.getWeightKg());
		}
		if (dto.getDailyCalorieGoal() != null) {
			profile.setDailyCalorieGoal(dto.getDailyCalorieGoal());
		}
		if (dto.getDailyProteinGoalG() != null) {
			profile.setDailyProteinGoalG(dto.getDailyProteinGoalG());
		}
		if (dto.getDailyCarbsGoalG() != null) {
			profile.setDailyCarbsGoalG(dto.getDailyCarbsGoalG());
		}
		if (dto.getDailyFatGoalG() != null) {
			profile.setDailyFatGoalG(dto.getDailyFatGoalG());
		}

		return toDto(userProfileRepository.save(profile));
	}

	@Transactional
	public UserProfileDto updateFromAiFields(Map<String, Object> fields) {
		UserProfileDto dto = new UserProfileDto();
		if (fields.containsKey("weight_kg")) {
			dto.setWeightKg(toDouble(fields.get("weight_kg")));
		}
		if (fields.containsKey("daily_calorie_goal")) {
			dto.setDailyCalorieGoal(toInteger(fields.get("daily_calorie_goal")));
		}
		if (fields.containsKey("name")) {
			dto.setName(String.valueOf(fields.get("name")));
		}
		if (fields.containsKey("daily_protein_goal_g")) {
			dto.setDailyProteinGoalG(toDouble(fields.get("daily_protein_goal_g")));
		}
		if (fields.containsKey("daily_carbs_goal_g")) {
			dto.setDailyCarbsGoalG(toDouble(fields.get("daily_carbs_goal_g")));
		}
		if (fields.containsKey("daily_fat_goal_g")) {
			dto.setDailyFatGoalG(toDouble(fields.get("daily_fat_goal_g")));
		}
		return updateProfile(dto);
	}

	@Transactional
	public UserProfile getOrCreateProfileEntity() {
		return userProfileRepository.findAll().stream().findFirst().orElseGet(() -> {
			UserProfile profile = new UserProfile();
			return userProfileRepository.save(profile);
		});
	}

	private UserProfileDto toDto(UserProfile profile) {
		UserProfileDto dto = new UserProfileDto();
		dto.setUserId(profile.getUserId());
		dto.setName(profile.getName());
		dto.setWeightKg(profile.getWeightKg());
		dto.setDailyCalorieGoal(profile.getDailyCalorieGoal());
		dto.setDailyProteinGoalG(profile.getDailyProteinGoalG());
		dto.setDailyCarbsGoalG(profile.getDailyCarbsGoalG());
		dto.setDailyFatGoalG(profile.getDailyFatGoalG());
		return dto;
	}

	private Double toDouble(Object value) {
		if (value instanceof Number number) {
			return number.doubleValue();
		}
		return Double.parseDouble(String.valueOf(value));
	}

	private Integer toInteger(Object value) {
		if (value instanceof Number number) {
			return number.intValue();
		}
		return Integer.parseInt(String.valueOf(value));
	}

}
