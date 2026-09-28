package com.example.calorie.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.calorie.entity.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, UUID> {

	Optional<Food> findByNameIgnoreCase(String name);

}
