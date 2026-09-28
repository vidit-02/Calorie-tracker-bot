package com.example.calorie.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.calorie.dto.dailyLog.DailyLogEntry;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "daily_logs")
public class DailyLog {

	@Id @GeneratedValue
	private UUID id;

	@Column(name = "log_date", nullable = false)
	private LocalDate logDate;

	@Column(name = "total_calories", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalCalories = BigDecimal.ZERO;

	@Column(name = "total_protein_g", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalProteinG = BigDecimal.ZERO;

	@Column(name = "total_carbs_g", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalCarbsG = BigDecimal.ZERO;

	@Column(name = "total_fat_g", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalFatG = BigDecimal.ZERO;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "entries", nullable = false, columnDefinition = "jsonb")
	private List<DailyLogEntry> entries = new ArrayList<>();


	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	public LocalDate getLogDate() { return logDate; }
	public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
	public BigDecimal getTotalCalories() { return totalCalories; }
	public void setTotalCalories(BigDecimal value) { totalCalories = value; }
	public BigDecimal getTotalProteinG() { return totalProteinG; }
	public void setTotalProteinG(BigDecimal value) { totalProteinG = value; }
	public BigDecimal getTotalCarbsG() { return totalCarbsG; }
	public void setTotalCarbsG(BigDecimal value) { totalCarbsG = value; }
	public BigDecimal getTotalFatG() { return totalFatG; }
	public void setTotalFatG(BigDecimal value) { totalFatG = value; }

	public List<DailyLogEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<DailyLogEntry> entries) {
		this.entries = entries;
	}
}
