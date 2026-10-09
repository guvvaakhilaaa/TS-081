-- =============================================================================
-- ECOIMPACT DATABASE SCHEMA (MySQL 8.0+ & H2 Compatible)
-- Comprehensive Schema for Gamified Carbon Reduction Web Application
-- =============================================================================

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    country VARCHAR(100) DEFAULT 'India',
    region_grid VARCHAR(100) DEFAULT 'India National Grid',
    eco_rank VARCHAR(50) DEFAULT 'Seedling',
    level INT DEFAULT 1,
    eco_xp INT DEFAULT 0,
    daily_streak INT DEFAULT 1,
    last_active_date DATE,
    opt_in_leaderboard BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    primary_transport VARCHAR(50) DEFAULT 'PETROL_CAR',
    household_size INT DEFAULT 3,
    budget_level VARCHAR(20) DEFAULT 'MEDIUM',
    feasibility_preference VARCHAR(20) DEFAULT 'HIGH',
    diet_type VARCHAR(50) DEFAULT 'MIXED',
    receive_notifications BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_pref_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emission_factors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    activity_type VARCHAR(100) NOT NULL,
    factor_value DECIMAL(10, 4) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    country_region VARCHAR(100) NOT NULL,
    source_reference VARCHAR(255) NOT NULL,
    publication_year INT NOT NULL,
    calculation_boundary VARCHAR(100) NOT NULL,
    version VARCHAR(20) DEFAULT 'v2024.1',
    assumptions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS activity_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    reporting_period VARCHAR(20) DEFAULT 'MONTHLY',
    record_date DATE NOT NULL,
    
    -- Transportation
    vehicle_type VARCHAR(50),
    distance_km DECIMAL(10, 2) DEFAULT 0.00,
    fuel_type VARCHAR(50),
    mileage_km_per_litre DECIMAL(10, 2) DEFAULT 15.00,
    fuel_consumed_litres DECIMAL(10, 2) DEFAULT 0.00,
    public_transport_km DECIMAL(10, 2) DEFAULT 0.00,
    flight_km DECIMAL(10, 2) DEFAULT 0.00,
    ev_energy_kwh DECIMAL(10, 2) DEFAULT 0.00,

    -- Residential & Energy
    electricity_kwh DECIMAL(10, 2) DEFAULT 0.00,
    grid_region VARCHAR(100) DEFAULT 'India National Grid',
    lpg_cylinders_or_kg DECIMAL(10, 2) DEFAULT 0.00,
    natural_gas_kwh DECIMAL(10, 2) DEFAULT 0.00,

    -- Agriculture & Food
    diet_type VARCHAR(50) DEFAULT 'MIXED',
    meat_servings_per_week INT DEFAULT 4,
    dairy_servings_per_week INT DEFAULT 7,
    food_waste_kg DECIMAL(10, 2) DEFAULT 2.00,

    -- Shopping & Goods
    clothing_items_bought INT DEFAULT 2,
    electronics_bought INT DEFAULT 0,
    general_goods_spend_usd DECIMAL(10, 2) DEFAULT 50.00,

    -- Waste Management
    waste_generated_kg DECIMAL(10, 2) DEFAULT 15.00,
    recycling_percentage DECIMAL(5, 2) DEFAULT 20.00,
    composting_active BOOLEAN DEFAULT FALSE,

    -- Industrial (optional organizational boundary)
    is_industrial BOOLEAN DEFAULT FALSE,
    industrial_energy_kwh DECIMAL(12, 2) DEFAULT 0.00,
    industrial_fuel_litres DECIMAL(12, 2) DEFAULT 0.00,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_activity_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS carbon_assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_record_id BIGINT,
    assessment_date DATE NOT NULL,
    total_footprint_kg DECIMAL(12, 4) NOT NULL,
    total_footprint_tonnes DECIMAL(10, 4) NOT NULL,
    largest_category VARCHAR(50),
    annual_projection_tonnes DECIMAL(10, 4) NOT NULL,
    reporting_period VARCHAR(20) DEFAULT 'MONTHLY',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_assessment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS category_emissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    emissions_kg DECIMAL(12, 4) NOT NULL,
    percentage_contribution DECIMAL(6, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cat_emissions_assessment FOREIGN KEY (assessment_id) REFERENCES carbon_assessments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    reasoning TEXT NOT NULL,
    estimated_reduction_kg DECIMAL(10, 2) NOT NULL,
    potential_cost_savings_usd DECIMAL(10, 2) DEFAULT 0.00,
    difficulty_level VARCHAR(20) DEFAULT 'MEDIUM',
    implementation_time VARCHAR(50) DEFAULT '1-2 weeks',
    action_steps TEXT,
    ranking_score DECIMAL(8, 4) NOT NULL,
    status VARCHAR(30) DEFAULT 'SUGGESTED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rec_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS simulation_scenarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    scenario_name VARCHAR(150) NOT NULL,
    baseline_emissions_kg DECIMAL(12, 4) NOT NULL,
    proposed_emissions_kg DECIMAL(12, 4) NOT NULL,
    reduction_kg DECIMAL(12, 4) NOT NULL,
    reduction_percentage DECIMAL(6, 2) NOT NULL,
    changes_summary TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sim_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reduction_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plan_title VARCHAR(150) NOT NULL,
    baseline_emissions_kg DECIMAL(12, 4) NOT NULL,
    target_reduction_percentage DECIMAL(6, 2) NOT NULL,
    target_emissions_kg DECIMAL(12, 4) NOT NULL,
    duration_months INT DEFAULT 6,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(30) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_plan_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reduction_goals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plan_id BIGINT,
    title VARCHAR(150) NOT NULL,
    target_percentage DECIMAL(6, 2) NOT NULL,
    target_kg_reduction DECIMAL(10, 2) NOT NULL,
    deadline DATE NOT NULL,
    current_progress_percentage DECIMAL(6, 2) DEFAULT 0.00,
    status VARCHAR(30) DEFAULT 'IN_PROGRESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_goal_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_goal_plan FOREIGN KEY (plan_id) REFERENCES reduction_plans(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS action_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    goal_id BIGINT,
    recommendation_id BIGINT,
    action_name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    estimated_kg_saved DECIMAL(10, 2) NOT NULL,
    completed_date DATE NOT NULL,
    verification_status VARCHAR(50) DEFAULT 'USER_REPORTED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_action_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS progress_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    assessment_id BIGINT,
    period_label VARCHAR(50) NOT NULL,
    total_footprint_kg DECIMAL(12, 4) NOT NULL,
    transport_kg DECIMAL(10, 2) DEFAULT 0.00,
    energy_kg DECIMAL(10, 2) DEFAULT 0.00,
    food_kg DECIMAL(10, 2) DEFAULT 0.00,
    waste_kg DECIMAL(10, 2) DEFAULT 0.00,
    shopping_kg DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sequestration_estimates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    method_type VARCHAR(50) NOT NULL,
    biomass_or_quantity DECIMAL(12, 4) NOT NULL,
    carbon_fraction DECIMAL(6, 4) DEFAULT 0.5000,
    co2_stored_kg DECIMAL(12, 4) NOT NULL,
    uncertainty_percentage DECIMAL(5, 2) DEFAULT 15.00,
    methodology_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sequestration_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS missions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    xp_reward INT NOT NULL,
    frequency_type VARCHAR(20) DEFAULT 'DAILY',
    icon_name VARCHAR(50) DEFAULT 'leaf',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_missions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    mission_id BIGINT NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP,
    claim_status VARCHAR(30) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_um_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_um_mission FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE,
    CONSTRAINT uq_user_mission UNIQUE (user_id, mission_id)
);

CREATE TABLE IF NOT EXISTS achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    badge_key VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    xp_bonus INT NOT NULL,
    icon_name VARCHAR(50) DEFAULT 'trophy',
    required_criterion VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    achievement_id BIGINT NOT NULL,
    unlocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ua_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ua_achievement FOREIGN KEY (achievement_id) REFERENCES achievements(id) ON DELETE CASCADE,
    CONSTRAINT uq_user_achievement UNIQUE (user_id, achievement_id)
);

CREATE TABLE IF NOT EXISTS xp_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount INT NOT NULL,
    source_type VARCHAR(50) NOT NULL,
    reference_id BIGINT,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_xp_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes for high performance querying
CREATE INDEX idx_activity_user_date ON activity_records (user_id, record_date);
CREATE INDEX idx_assessment_user_date ON carbon_assessments (user_id, assessment_date);
CREATE INDEX idx_recommendations_user_score ON recommendations (user_id, ranking_score);
CREATE INDEX idx_progress_user_created ON progress_history (user_id, created_at);
CREATE INDEX idx_user_missions_user_comp ON user_missions (user_id, completed);
