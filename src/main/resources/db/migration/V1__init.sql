-- =========================================================
-- FOOD
-- =========================================================

CREATE TABLE food (
    id UUID PRIMARY KEY,

    name VARCHAR(100) NOT NULL UNIQUE,

    brand VARCHAR(50),

    category VARCHAR(100),

    serving_size DECIMAL(10,3) NOT NULL,

    serving_unit VARCHAR(10) NOT NULL,

    calories DECIMAL,

    protein_g DECIMAL,

    carbs_g DECIMAL,

    fat_g DECIMAL
);


-- =========================================================
-- MEALS
-- =========================================================

CREATE TABLE meals (
    id UUID PRIMARY KEY,

    name VARCHAR(255) NOT NULL UNIQUE,

    description TEXT,

    total_calories DECIMAL(10,2) NOT NULL DEFAULT 0,

    total_protein_g DECIMAL(10,2) NOT NULL DEFAULT 0,

    total_carbs_g DECIMAL(10,2) NOT NULL DEFAULT 0,

    total_fat_g DECIMAL(10,2) NOT NULL DEFAULT 0
);


-- =========================================================
-- MEAL ITEMS
-- =========================================================

CREATE TABLE meal_items (
    id UUID PRIMARY KEY,

    meal_id UUID NOT NULL,

    food_id UUID NOT NULL,

    quantity DECIMAL(10,3) NOT NULL,

    unit VARCHAR(10) NOT NULL,

    CONSTRAINT fk_meal_item_meal
        FOREIGN KEY (meal_id)
        REFERENCES meals(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_meal_item_food
        FOREIGN KEY (food_id)
        REFERENCES food(id)
);


-- =========================================================
-- USER PROFILE
-- =========================================================

CREATE TABLE user_profile (
    user_id UUID PRIMARY KEY,

    name VARCHAR(50),

    weight_kg DOUBLE PRECISION,

    daily_calorie_goal INTEGER,

    daily_protein_goal_g DOUBLE PRECISION,

    daily_carbs_goal_g DOUBLE PRECISION,

    daily_fat_goal_g DOUBLE PRECISION
);


-- =========================================================
-- DAILY LOGS
-- =========================================================

CREATE TABLE daily_logs (
    id UUID PRIMARY KEY,

    log_date DATE NOT NULL,

    total_calories DECIMAL(10,2) NOT NULL DEFAULT 0,

    total_protein_g DECIMAL(10,2) NOT NULL DEFAULT 0,

    total_carbs_g DECIMAL(10,2) NOT NULL DEFAULT 0,

    total_fat_g DECIMAL(10,2) NOT NULL DEFAULT 0,

    entries JSONB NOT NULL,

    CONSTRAINT uq_daily_log_date UNIQUE (log_date)
);