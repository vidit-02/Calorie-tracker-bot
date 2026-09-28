package com.example.calorie.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "food_alias",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "alias")
        }
)
public class FoodAlias {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(nullable = false, length = 100)
    private String alias;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    // getters/setters
}