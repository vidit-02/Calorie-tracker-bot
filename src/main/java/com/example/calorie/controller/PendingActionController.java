package com.example.calorie.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.calorie.dto.ConfirmRequest;
import com.example.calorie.service.PendingActionService;

@RestController
@RequestMapping("/api/confirm")
public class PendingActionController {

	private final PendingActionService pendingActionService;

	public PendingActionController(PendingActionService pendingActionService) {
		this.pendingActionService = pendingActionService;
	}

	@PostMapping
	public ResponseEntity<Map<String, String>> confirm(@RequestBody ConfirmRequest request) {
		if (request.getActionId() == null) {
			return ResponseEntity
					.badRequest()
					.body(Map.of("error", "actionId is required"));
		}

		if (!request.isConfirm()) {
			pendingActionService.cancelAction(
					request.getActionId()
			);

			return ResponseEntity.ok(
					Map.of("status", "cancelled")
			);
		}
		String message = pendingActionService.executeConfirmed(request.getActionId());
		return ResponseEntity.ok(Map.of("status", "ok", "message", message));
	}

}
