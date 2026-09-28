package com.example.calorie.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.calorie.dto.dailyLog.DailyLogDto;
import com.example.calorie.service.DailyLogService;

@RestController
@RequestMapping("/api/logs")
public class DailyLogController {

	private final DailyLogService logService;

	public DailyLogController(DailyLogService logService) {
		this.logService = logService;
	}

	// Get daily log
	@GetMapping
	public DailyLogDto getDailyLog(
			@RequestParam(required = false) LocalDate date) {

		if (date == null) {
			date = LocalDate.now();
		}

		return logService.getDailyLog(date);
	}


	// Remove a specific log entry
	@DeleteMapping("/{entryId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeLogEntry(@PathVariable UUID entryId) {

		logService.removeLogEntry(entryId);
	}

}
