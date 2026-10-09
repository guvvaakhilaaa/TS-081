package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_records")
public class ActivityRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "reporting_period")
    private String reportingPeriod = "MONTHLY";

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate = LocalDate.now();

    // Transportation
    @Column(name = "vehicle_type")
    private String vehicleType = "PETROL_CAR";

    @Column(name = "distance_km", precision = 10, scale = 2)
    private BigDecimal distanceKm = BigDecimal.ZERO;

    @Column(name = "fuel_type")
    private String fuelType = "PETROL";

    @Column(name = "mileage_km_per_litre", precision = 10, scale = 2)
    private BigDecimal mileageKmPerLitre = new BigDecimal("15.00");

    @Column(name = "fuel_consumed_litres", precision = 10, scale = 2)
    private BigDecimal fuelConsumedLitres = BigDecimal.ZERO;

    @Column(name = "public_transport_km", precision = 10, scale = 2)
    private BigDecimal publicTransportKm = BigDecimal.ZERO;

    @Column(name = "flight_km", precision = 10, scale = 2)
    private BigDecimal flightKm = BigDecimal.ZERO;

    @Column(name = "ev_energy_kwh", precision = 10, scale = 2)
    private BigDecimal evEnergyKwh = BigDecimal.ZERO;

    // Residential & Energy
    @Column(name = "electricity_kwh", precision = 10, scale = 2)
    private BigDecimal electricityKwh = BigDecimal.ZERO;

    @Column(name = "grid_region")
    private String gridRegion = "India National Grid";

    @Column(name = "lpg_cylinders_or_kg", precision = 10, scale = 2)
    private BigDecimal lpgCylindersOrKg = BigDecimal.ZERO;

    @Column(name = "natural_gas_kwh", precision = 10, scale = 2)
    private BigDecimal naturalGasKwh = BigDecimal.ZERO;

    // Agriculture & Food
    @Column(name = "diet_type")
    private String dietType = "MIXED";

    @Column(name = "meat_servings_per_week")
    private Integer meatServingsPerWeek = 4;

    @Column(name = "dairy_servings_per_week")
    private Integer dairyServingsPerWeek = 7;

    @Column(name = "food_waste_kg", precision = 10, scale = 2)
    private BigDecimal foodWasteKg = new BigDecimal("2.00");

    // Shopping & Goods
    @Column(name = "clothing_items_bought")
    private Integer clothingItemsBought = 2;

    @Column(name = "electronics_bought")
    private Integer electronicsBought = 0;

    @Column(name = "general_goods_spend_usd", precision = 10, scale = 2)
    private BigDecimal generalGoodsSpendUsd = new BigDecimal("50.00");

    // Waste Management
    @Column(name = "waste_generated_kg", precision = 10, scale = 2)
    private BigDecimal wasteGeneratedKg = new BigDecimal("15.00");

    @Column(name = "recycling_percentage", precision = 5, scale = 2)
    private BigDecimal recyclingPercentage = new BigDecimal("20.00");

    @Column(name = "composting_active")
    private Boolean compostingActive = false;

    // Industrial / Organizational Option
    @Column(name = "is_industrial")
    private Boolean isIndustrial = false;

    @Column(name = "industrial_energy_kwh", precision = 12, scale = 2)
    private BigDecimal industrialEnergyKwh = BigDecimal.ZERO;

    @Column(name = "industrial_fuel_litres", precision = 12, scale = 2)
    private BigDecimal industrialFuelLitres = BigDecimal.ZERO;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ActivityRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getReportingPeriod() { return reportingPeriod; }
    public void setReportingPeriod(String reportingPeriod) { this.reportingPeriod = reportingPeriod; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public BigDecimal getMileageKmPerLitre() { return mileageKmPerLitre; }
    public void setMileageKmPerLitre(BigDecimal mileageKmPerLitre) { this.mileageKmPerLitre = mileageKmPerLitre; }

    public BigDecimal getFuelConsumedLitres() { return fuelConsumedLitres; }
    public void setFuelConsumedLitres(BigDecimal fuelConsumedLitres) { this.fuelConsumedLitres = fuelConsumedLitres; }

    public BigDecimal getPublicTransportKm() { return publicTransportKm; }
    public void setPublicTransportKm(BigDecimal publicTransportKm) { this.publicTransportKm = publicTransportKm; }

    public BigDecimal getFlightKm() { return flightKm; }
    public void setFlightKm(BigDecimal flightKm) { this.flightKm = flightKm; }

    public BigDecimal getEvEnergyKwh() { return evEnergyKwh; }
    public void setEvEnergyKwh(BigDecimal evEnergyKwh) { this.evEnergyKwh = evEnergyKwh; }

    public BigDecimal getElectricityKwh() { return electricityKwh; }
    public void setElectricityKwh(BigDecimal electricityKwh) { this.electricityKwh = electricityKwh; }

    public String getGridRegion() { return gridRegion; }
    public void setGridRegion(String gridRegion) { this.gridRegion = gridRegion; }

    public BigDecimal getLpgCylindersOrKg() { return lpgCylindersOrKg; }
    public void setLpgCylindersOrKg(BigDecimal lpgCylindersOrKg) { this.lpgCylindersOrKg = lpgCylindersOrKg; }

    public BigDecimal getNaturalGasKwh() { return naturalGasKwh; }
    public void setNaturalGasKwh(BigDecimal naturalGasKwh) { this.naturalGasKwh = naturalGasKwh; }

    public String getDietType() { return dietType; }
    public void setDietType(String dietType) { this.dietType = dietType; }

    public Integer getMeatServingsPerWeek() { return meatServingsPerWeek; }
    public void setMeatServingsPerWeek(Integer meatServingsPerWeek) { this.meatServingsPerWeek = meatServingsPerWeek; }

    public Integer getDairyServingsPerWeek() { return dairyServingsPerWeek; }
    public void setDairyServingsPerWeek(Integer dairyServingsPerWeek) { this.dairyServingsPerWeek = dairyServingsPerWeek; }

    public BigDecimal getFoodWasteKg() { return foodWasteKg; }
    public void setFoodWasteKg(BigDecimal foodWasteKg) { this.foodWasteKg = foodWasteKg; }

    public Integer getClothingItemsBought() { return clothingItemsBought; }
    public void setClothingItemsBought(Integer clothingItemsBought) { this.clothingItemsBought = clothingItemsBought; }

    public Integer getElectronicsBought() { return electronicsBought; }
    public void setElectronicsBought(Integer electronicsBought) { this.electronicsBought = electronicsBought; }

    public BigDecimal getGeneralGoodsSpendUsd() { return generalGoodsSpendUsd; }
    public void setGeneralGoodsSpendUsd(BigDecimal generalGoodsSpendUsd) { this.generalGoodsSpendUsd = generalGoodsSpendUsd; }

    public BigDecimal getWasteGeneratedKg() { return wasteGeneratedKg; }
    public void setWasteGeneratedKg(BigDecimal wasteGeneratedKg) { this.wasteGeneratedKg = wasteGeneratedKg; }

    public BigDecimal getRecyclingPercentage() { return recyclingPercentage; }
    public void setRecyclingPercentage(BigDecimal recyclingPercentage) { this.recyclingPercentage = recyclingPercentage; }

    public Boolean getCompostingActive() { return compostingActive; }
    public void setCompostingActive(Boolean compostingActive) { this.compostingActive = compostingActive; }

    public Boolean getIsIndustrial() { return isIndustrial; }
    public void setIsIndustrial(Boolean isIndustrial) { this.isIndustrial = isIndustrial; }

    public BigDecimal getIndustrialEnergyKwh() { return industrialEnergyKwh; }
    public void setIndustrialEnergyKwh(BigDecimal industrialEnergyKwh) { this.industrialEnergyKwh = industrialEnergyKwh; }

    public BigDecimal getIndustrialFuelLitres() { return industrialFuelLitres; }
    public void setIndustrialFuelLitres(BigDecimal industrialFuelLitres) { this.industrialFuelLitres = industrialFuelLitres; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
