package com.example.calorie.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.calorie.dto.UserProfileDto;
import com.example.calorie.service.UserService;

@RestController
@RequestMapping("/api/profile")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public UserProfileDto getProfile() {
		return userService.getProfile();
	}

	@PutMapping
	public UserProfileDto updateProfile(@Valid @RequestBody UserProfileDto dto) {
		return userService.updateProfile(dto);
	}

}
