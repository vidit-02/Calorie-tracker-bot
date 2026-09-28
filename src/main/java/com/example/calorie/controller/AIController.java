package com.example.calorie.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.calorie.dto.AIPayload;
import com.example.calorie.model.PendingAction;
import com.example.calorie.service.PendingActionService;

@RestController
@RequestMapping("/api/ai")
public class AIController {

	private final PendingActionService pendingActionService;

	public AIController(PendingActionService pendingActionService) {
		this.pendingActionService = pendingActionService;
	}

	@PostMapping
	public ResponseEntity<?> handleAI(@RequestBody AIPayload payload) {
		if (Boolean.TRUE.equals(payload.getNeedsClarification())) {
			return ResponseEntity.ok(Map.of(
					"needs_clarification", true,
					"message", payload.getClarification() != null ? payload.getClarification() : ""));
		}
		PendingAction action = pendingActionService.createAction(payload);
		return ResponseEntity.ok(Map.of(
				"actionId", action.getId(),
				"message", action.getConfirmationMessage()));
	}

}
