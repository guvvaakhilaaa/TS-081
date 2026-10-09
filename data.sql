-- =============================================================================
-- ECOIMPACT INITIAL SEED DATA
-- Documented Emission Factors, Default Missions, and Badges
-- =============================================================================

-- 1. Documented Emission Factors (Verified from CEA, DEFRA, IPCC, EPA)
INSERT INTO emission_factors (category, activity_type, factor_value, unit, country_region, source_reference, publication_year, calculation_boundary, version, assumptions) VALUES
('TRANSPORTATION', 'PETROL_CAR', 2.3100, 'kg CO2e / litre', 'Global/India', 'IPCC Tier 1 Default / CEA', 2023, 'Tank-to-Wheel (Direct)', 'v2024.1', 'Average petrol density 0.74 kg/L with 99% carbon oxidation'),
('TRANSPORTATION', 'DIESEL_CAR', 2.6800, 'kg CO2e / litre', 'Global/India', 'DEFRA / IPCC Tier 1', 2023, 'Tank-to-Wheel (Direct)', 'v2024.1', 'Average diesel emission factor for passenger vehicles'),
('TRANSPORTATION', 'CNG_VEHICLE', 1.8200, 'kg CO2e / kg', 'India', 'ARAI / MoPNG India', 2023, 'Tank-to-Wheel', 'v2024.1', 'Compressed Natural Gas fuel combustion'),
('TRANSPORTATION', 'ELECTRIC_VEHICLE', 0.1200, 'kg CO2e / km', 'India National Grid', 'CEA CO2 Baseline Database v19', 2023, 'Well-to-Wheel (Indirect Grid)', 'v2024.1', 'Assuming average EV efficiency of 0.15 kWh/km on Indian grid (0.82 kg/kWh)'),
('TRANSPORTATION', 'CITY_BUS', 0.0890, 'kg CO2e / passenger-km', 'Global/India', 'DEFRA Transport Factors', 2023, 'Well-to-Wheel', 'v2024.1', 'Average occupancy city public bus transit'),
('TRANSPORTATION', 'TRAIN_METRO', 0.0350, 'kg CO2e / passenger-km', 'India', 'Indian Railways / DMRC Annual Report', 2023, 'Indirect Electric Traction', 'v2024.1', 'Electric suburban train & metro mass transit'),
('TRANSPORTATION', 'DOMESTIC_FLIGHT', 0.2540, 'kg CO2e / passenger-km', 'Global', 'ICAO Carbon Calculator / DEFRA', 2023, 'Well-to-Wake (Includes RF multiplier)', 'v2024.1', 'Short-haul domestic flight including radiative forcing effect'),
('TRANSPORTATION', 'INTERNATIONAL_FLIGHT', 0.1950, 'kg CO2e / passenger-km', 'Global', 'ICAO Carbon Calculator / DEFRA', 2023, 'Well-to-Wake', 'v2024.1', 'Long-haul international flight economy class'),

('RESIDENTIAL', 'ELECTRICITY_INDIA', 0.8200, 'kg CO2e / kWh', 'India National Grid', 'CEA CO2 Baseline Database v19', 2023, 'Generation + Transmission Losses', 'v2024.1', 'Combined margin grid emission factor for India'),
('RESIDENTIAL', 'ELECTRICITY_US', 0.3860, 'kg CO2e / kWh', 'USA eGRID Average', 'US EPA eGRID2022', 2023, 'Generation Boundary', 'v2024.1', 'US national weighted average electricity factor'),
('RESIDENTIAL', 'ELECTRICITY_EU', 0.2500, 'kg CO2e / kWh', 'EU Average', 'EEA Greenhouse Gas Data', 2023, 'Generation Boundary', 'v2024.1', 'European Union power mix average'),
('RESIDENTIAL', 'LPG_COOKING', 2.9840, 'kg CO2e / kg', 'Global', 'IPCC Guidelines Vol 2 Energy', 2023, 'Combustion (Direct Scope 1)', 'v2024.1', 'Domestic liquified petroleum gas for cooking cylinder (14.2 kg cylinder = 42.37 kg CO2e)'),
('RESIDENTIAL', 'NATURAL_GAS_PNG', 0.2020, 'kg CO2e / kWh', 'Global/India', 'DEFRA / PNGRB', 2023, 'Combustion (Direct)', 'v2024.1', 'Piped natural gas domestic heating and cooking'),

('FOOD', 'MEAT_HEAVY_DIET', 7.2000, 'kg CO2e / day', 'Global', 'Poore & Nemecek (Science 2018)', 2023, 'Farm-to-Fork Lifecycle', 'v2024.1', 'Daily emissions for diet with high beef/mutton/pork consumption'),
('FOOD', 'MEDIUM_MEAT_DIET', 5.6000, 'kg CO2e / day', 'Global', 'Poore & Nemecek (Science 2018)', 2023, 'Farm-to-Fork Lifecycle', 'v2024.1', 'Moderate poultry/fish/meat consumer'),
('FOOD', 'VEGETARIAN_DIET', 3.8000, 'kg CO2e / day', 'Global/India', 'Poore & Nemecek (Science 2018)', 2023, 'Farm-to-Fork Lifecycle', 'v2024.1', 'Lacto-ovo vegetarian diet with dairy inclusion'),
('FOOD', 'VEGAN_DIET', 2.9000, 'kg CO2e / day', 'Global', 'Poore & Nemecek (Science 2018)', 2023, 'Farm-to-Fork Lifecycle', 'v2024.1', 'Strict plant-based diet excluding animal agriculture'),
('FOOD', 'FOOD_WASTE', 2.5000, 'kg CO2e / kg food wasted', 'Global', 'UN FAO Food Wastage Footprint', 2023, 'Embedded Agricultural + Landfill Methane', 'v2024.1', 'Municipal organic food discarded to landfill with anaerobic methane release'),

('SHOPPING', 'CLOTHING_FAST_FASHION', 15.0000, 'kg CO2e / item', 'Global', 'WRAP / Ellen MacArthur Foundation', 2023, 'Cradle-to-Gate Manufacturing', 'v2024.1', 'Average cotton/polyester blended garment production and supply chain'),
('SHOPPING', 'ELECTRONICS_SMARTPHONE', 60.0000, 'kg CO2e / device', 'Global', 'Apple/Samsung Environmental Reports', 2023, 'Cradle-to-Grave Lifecycle', 'v2024.1', 'Embodied carbon in semiconductors, display, battery and assembly'),
('SHOPPING', 'GENERAL_GOODS_SPEND', 0.3500, 'kg CO2e / USD spend', 'Global', 'Carnegie Mellon EIO-LCA Proxy', 2023, 'Economic Input-Output Proxy', 'v2024.1', 'Standard manufactured retail consumer goods proxy estimate'),

('WASTE', 'MIXED_MUNICIPAL_WASTE', 0.5800, 'kg CO2e / kg waste', 'Global/India', 'IPCC Waste Model / CPCB India', 2023, 'Landfill Anaerobic Decay', 'v2024.1', 'Unsegregated municipal solid waste decomposing in landfill'),
('WASTE', 'COMPOSTED_ORGANIC', 0.0800, 'kg CO2e / kg organic waste', 'Global', 'IPCC Tier 1 Biological Treatment', 2023, 'Aerobic Composting Boundary', 'v2024.1', 'Managed aerobic composting, saving methane emissions vs landfill'),
('WASTE', 'RECYCLED_MATERIALS', -0.4500, 'kg CO2e / kg recycled', 'Global', 'DEFRA Waste Benefit Factors', 2023, 'Virgin Material Displacement Avoidance', 'v2024.1', 'Virgin material extraction avoidance credit for recycled plastics/metals/paper');

-- 2. Gaming Missions
INSERT INTO missions (title, description, category, xp_reward, frequency_type, icon_name, is_active) VALUES
('Switch to Public Transit or Metro', 'Take the bus, metro, or cycle for at least 15 km this week instead of driving a personal fuel car.', 'TRANSPORTATION', 50, 'WEEKLY', 'bus', TRUE),
('Unplug Phantom Power & Idle Devices', 'Switch off standby chargers, idle appliances, and run natural ventilation for 24 hours.', 'RESIDENTIAL', 30, 'DAILY', 'power', TRUE),
('Meat-Free Green Monday', 'Enjoy a 100% vegetarian or plant-based meal today to reduce agricultural methane emissions.', 'FOOD', 40, 'WEEKLY', 'leaf', TRUE),
('Zero Food Waste Day', 'Finish all prepared meals, store leftovers properly, and discard zero edible food today.', 'FOOD', 35, 'DAILY', 'utensils', TRUE),
('Waste Segregation & Composting', 'Segregate dry recyclables and initiate organic kitchen waste composting.', 'WASTE', 45, 'WEEKLY', 'recycle', TRUE),
('Conduct Monthly Carbon Review', 'Calculate and review your full carbon footprint assessment for the current cycle.', 'ASSESSMENT', 60, 'MONTHLY', 'calculator', TRUE),
('Activate a 6-Month Reduction Plan', 'Select an actionable reduction goal in the Goal Planning module and start milestone 1.', 'GOALS', 75, 'ONETIME', 'target', TRUE);

-- 3. Gamification Badges & Achievements
INSERT INTO achievements (badge_key, title, description, xp_bonus, icon_name, required_criterion) VALUES
('FIRST_FOOTPRINT', 'First Footprint Pioneer', 'Completed your very first comprehensive carbon footprint calculation.', 50, 'footprints', 'CALCULATE_FIRST_ASSESSMENT'),
('ENERGY_SAVER', 'Watt Whisperer', 'Reduced home electricity usage or logged an energy-saving efficiency action.', 75, 'zap', 'ACTION_ENERGY_SAVED'),
('ECO_COMMUTER', 'Green Commuter', 'Logged 50+ km of public transport, cycling, or shared transit.', 80, 'bike', 'ACTION_TRANSIT_COMMUTE'),
('WASTE_WARRIOR', 'Zero-Waste Champion', 'Maintained 80%+ recycling or active composting for your household waste.', 70, 'trash-2', 'ACTION_WASTE_REDUCTION'),
('SEVEN_DAY_STREAK', 'Eco Devotee (7-Day Streak)', 'Maintained an unbroken daily climate action logging streak for 7 days.', 120, 'flame', 'STREAK_7_DAYS'),
('CARBON_CHAMPION', 'Planet Guardian Hero', 'Achieved a verified 15%+ carbon footprint reduction in your active plan.', 200, 'award', 'REDUCTION_15_PERCENT');

-- 4. Default Demo User: "Aarav Sharma" (B.Tech PBL Hackathon Persona)
INSERT INTO users (id, username, email, password_hash, full_name, country, region_grid, eco_rank, level, eco_xp, daily_streak, last_active_date, opt_in_leaderboard) VALUES
(1, 'aarav_eco', 'aarav.sharma@ecoimpact.org', '$2a$10$eO1v0iY3yO1pA6v7bFhV9uX6xXJ9V7cR.8y5R5q6j4L8w5F6v8Q4O', 'Aarav Sharma', 'India', 'India National Grid', 'Sprout', 2, 185, 4, CURRENT_DATE, TRUE);

INSERT INTO user_preferences (user_id, primary_transport, household_size, budget_level, feasibility_preference, diet_type, receive_notifications) VALUES
(1, 'PETROL_CAR', 3, 'MEDIUM', 'HIGH', 'MIXED', TRUE);

-- Seed Initial Activity Record for Aarav
INSERT INTO activity_records (id, user_id, reporting_period, record_date, vehicle_type, distance_km, fuel_type, mileage_km_per_litre, fuel_consumed_litres, public_transport_km, flight_km, ev_energy_kwh, electricity_kwh, grid_region, lpg_cylinders_or_kg, natural_gas_kwh, diet_type, meat_servings_per_week, dairy_servings_per_week, food_waste_kg, clothing_items_bought, electronics_bought, general_goods_spend_usd, waste_generated_kg, recycling_percentage, composting_active) VALUES
(1, 1, 'MONTHLY', CURRENT_DATE, 'PETROL_CAR', 450.00, 'PETROL', 15.00, 30.00, 60.00, 0.00, 0.00, 240.00, 'India National Grid', 14.20, 0.00, 'MIXED', 4, 7, 3.50, 2, 0, 45.00, 20.00, 25.00, FALSE);

-- Seed Baseline Carbon Assessment
-- Calculations:
-- Transport: 30L petrol * 2.31 = 69.30 kg + 60 km bus * 0.089 = 5.34 kg => 74.64 kg
-- Energy: 240 kWh * 0.82 = 196.80 kg + 14.2 kg LPG * 2.984 = 42.37 kg => 239.17 kg
-- Food: (30 days * 5.6 kg/day = 168 kg) + (3.5 kg waste * 2.5 = 8.75 kg) => 176.75 kg
-- Shopping: (2 garments * 15 = 30 kg) + ($45 * 0.35 = 15.75 kg) => 45.75 kg
-- Waste: (20 kg generated: 5 kg recycled (-0.45*5 = -2.25), 15 kg landfill (15*0.58 = 8.70)) => 6.45 kg
-- Total = 74.64 + 239.17 + 176.75 + 45.75 + 6.45 = 542.76 kg CO2e = 0.5428 tonnes / month
-- Annual projection = 0.5428 * 12 = 6.5136 tonnes
INSERT INTO carbon_assessments (id, user_id, activity_record_id, assessment_date, total_footprint_kg, total_footprint_tonnes, largest_category, annual_projection_tonnes, reporting_period) VALUES
(1, 1, 1, CURRENT_DATE, 542.7600, 0.5428, 'RESIDENTIAL', 6.5136, 'MONTHLY');

INSERT INTO category_emissions (assessment_id, category, emissions_kg, percentage_contribution) VALUES
(1, 'RESIDENTIAL', 239.1700, 44.07),
(1, 'FOOD', 176.7500, 32.57),
(1, 'TRANSPORTATION', 74.6400, 13.75),
(1, 'SHOPPING', 45.7500, 8.43),
(1, 'WASTE', 6.4500, 1.18);

-- Seed User Missions for Aarav
INSERT INTO user_missions (user_id, mission_id, completed, completed_at, claim_status) VALUES
(1, 1, TRUE, CURRENT_TIMESTAMP, 'CLAIMED'),
(1, 2, TRUE, CURRENT_TIMESTAMP, 'CLAIMED'),
(1, 3, FALSE, NULL, 'PENDING'),
(1, 4, FALSE, NULL, 'PENDING'),
(1, 5, FALSE, NULL, 'PENDING'),
(1, 6, TRUE, CURRENT_TIMESTAMP, 'CLAIMED'),
(1, 7, FALSE, NULL, 'PENDING');

-- Seed User Achievements
INSERT INTO user_achievements (user_id, achievement_id, unlocked_at) VALUES
(1, 1, CURRENT_TIMESTAMP),
(1, 2, CURRENT_TIMESTAMP);

-- Seed XP Transactions
INSERT INTO xp_transactions (user_id, amount, source_type, reference_id, description) VALUES
(1, 50, 'ACHIEVEMENT', 1, 'Unlocked badge: First Footprint Pioneer'),
(1, 75, 'ACHIEVEMENT', 2, 'Unlocked badge: Watt Whisperer'),
(1, 60, 'MISSION', 6, 'Completed monthly carbon assessment review');
