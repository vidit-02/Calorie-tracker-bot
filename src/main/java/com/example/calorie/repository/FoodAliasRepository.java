package com.example.calorie.repository;

import com.example.calorie.entity.FoodAlias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FoodAliasRepository extends JpaRepository<FoodAlias, UUID> {

    Optional<FoodAlias> findByAliasIgnoreCase(String alias);
}
