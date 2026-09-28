package com.example.calorie.model;

import java.util.UUID;

import com.example.calorie.dto.AIPayload;

public class PendingAction {

	private final UUID id;

	private final AIPayload payload;

	private final String confirmationMessage;

	public PendingAction(
			UUID id,
			AIPayload payload,
			String confirmationMessage) {

		this.id = id;
		this.payload = payload;
		this.confirmationMessage = confirmationMessage;
	}

	public UUID getId() {
		return id;
	}

	public AIPayload getPayload() {
		return payload;
	}

	public String getConfirmationMessage() {
		return confirmationMessage;
	}
}
