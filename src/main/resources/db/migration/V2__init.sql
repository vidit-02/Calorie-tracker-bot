CREATE TABLE food_alias (
    id UUID PRIMARY KEY,
    food_id UUID NOT NULL,
    alias VARCHAR(100) NOT NULL,

    CONSTRAINT fk_food_alias_food
        FOREIGN KEY (food_id)
        REFERENCES food(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_food_alias_alias
        UNIQUE (alias)
);