package com.example.calorie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.calorie.dto.food.CreateFoodRequest;
import com.example.calorie.dto.food.FoodResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.calorie.dto.FoodDto;
import com.example.calorie.service.FoodService;

@WebMvcTest(FoodController.class)
class FoodControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private FoodService foodService;

	@Test
	void createFoodReturnsCreated() throws Exception {
		FoodResponse saved = new FoodResponse();
		saved.setId(0x1L);
		saved.setName("Moong Dal");
		when(foodService.create(any(CreateFoodRequest.class))).thenReturn(saved);

		mockMvc.perform(post("/api/foods")
			.contentType(MediaType.APPLICATION_JSON)
			.content("""
					{"name":"Moong Dal","serving_size":100,"serving_unit":"g"}
					"""))
			.andExpect(status().isCreated());
	}

}
