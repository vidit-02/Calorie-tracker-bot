package com.example.calorie.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.calorie.entity.Meal;

@Repository
public interface MealRepository extends JpaRepository<Meal, UUID> {

	@Query("""
    SELECT DISTINCT m
    FROM Meal m
    LEFT JOIN FETCH m.items i
    LEFT JOIN FETCH i.food
    WHERE m.id = :id
""")
	Optional<Meal> findByIdWithItems(@Param("id") UUID id);

	Optional<Meal> findByNameIgnoreCase(String name);

}
