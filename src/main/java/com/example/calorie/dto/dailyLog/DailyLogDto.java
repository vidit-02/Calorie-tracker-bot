package com.example.calorie.dto.dailyLog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyLogDto {
	private UUID id;

	@JsonProperty("log_date") private LocalDate logDate;

	@JsonProperty("total_calories") private BigDecimal totalCalories;

	@JsonProperty("total_protein_g") private BigDecimal totalProteinG;

	@JsonProperty("total_carbs_g") private BigDecimal totalCarbsG;

	@JsonProperty("total_fat_g") private BigDecimal totalFatG;

	private List<DailyLogEntryResponse> entries = new ArrayList<>();

	public UUID getId() { return id; }
	public void setId(UUID value) { id = value; }
	public LocalDate getLogDate() { return logDate; }
	public void setLogDate(LocalDate value) { logDate = value; }
	public BigDecimal getTotalCalories() { return totalCalories; }
	public void setTotalCalories(BigDecimal value) { totalCalories = value; }
	public BigDecimal getTotalProteinG() { return totalProteinG; }
	public void setTotalProteinG(BigDecimal value) { totalProteinG = value; }
	public BigDecimal getTotalCarbsG() { return totalCarbsG; }
	public void setTotalCarbsG(BigDecimal value) { totalCarbsG = value; }
	public BigDecimal getTotalFatG() { return totalFatG; }
	public void setTotalFatG(BigDecimal value) { totalFatG = value; }
	public List<DailyLogEntryResponse> getEntries() { return entries; }
	public void setEntries(List<DailyLogEntryResponse> value) { entries = value; }
}
