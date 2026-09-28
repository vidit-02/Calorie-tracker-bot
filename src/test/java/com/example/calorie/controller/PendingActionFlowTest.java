package com.example.calorie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.calorie.dto.AIPayload;
import com.example.calorie.model.PendingAction;
import com.example.calorie.service.PendingActionService;

@WebMvcTest(controllers = { AIController.class, PendingActionController.class })
class PendingActionFlowTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PendingActionService pendingActionService;

	@Test
	void aiThenConfirmFlow() throws Exception {
		UUID actionId = UUID.randomUUID();
		AIPayload payload = new AIPayload();
		payload.setIntent("LOG_FOOD");
		PendingAction action = new PendingAction(actionId, payload, "Confirm?");
		when(pendingActionService.createAction(any(AIPayload.class))).thenReturn(action);
		when(pendingActionService.executeConfirmed(actionId)).thenReturn("Entry logged successfully.");

		mockMvc.perform(post("/api/ai")
			.contentType(MediaType.APPLICATION_JSON)
			.content("""
					{"intent":"LOG_FOOD","items":[{"food_name":"paneer","quantity":150,"unit":"g"}],"meal_type":"DINNER"}
					"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.actionId").value(actionId.toString()));

		mockMvc.perform(post("/api/confirm")
			.contentType(MediaType.APPLICATION_JSON)
			.content("{\"actionId\":\"" + actionId + "\",\"confirm\":true}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("ok"));

		verify(pendingActionService).executeConfirmed(actionId);
	}

}
