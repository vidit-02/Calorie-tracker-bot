package com.example.calorie.dto;

import java.util.UUID;

public class ConfirmRequest {

	public UUID actionId;

	public boolean confirm;

	public UUID getActionId() {
		return actionId;
	}

	public void setActionId(UUID actionId) {
		this.actionId = actionId;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}
}
