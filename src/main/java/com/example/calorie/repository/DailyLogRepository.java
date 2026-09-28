package com.example.calorie.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.calorie.entity.DailyLog;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, UUID> {

//	List<DailyLog> findByUserProfileUserIdAndLogDate(UUID userId, LocalDate logDate);
//
//	Optional<DailyLog> findFirstByUserProfileUserIdAndLogDate(UUID userId, LocalDate logDate);

	Optional<DailyLog> findByLogDate(LocalDate logDate);

}
