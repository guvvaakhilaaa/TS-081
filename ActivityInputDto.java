package com.ecoimpact.dto;

import java.math.BigDecimal;

public class ActivityInputDto {
    private Long userId = 1L;
    private String reportingPeriod = "MONTHLY"; // MONTHLY, WEEKLY, DAILY

    // Transportation
    private String vehicleType = "PETROL_CAR"; // PETROL_CAR, DIESEL_CAR, CNG_VEHICLE, ELECTRIC_VEHICLE
    private BigDecimal distanceKm = BigDecimal.ZERO;
    private BigDecimal mileageKmPerLitre = new BigDecimal("15.00");
    private BigDecimal fuelConsumedLitres = BigDecimal.ZERO;
    private BigDecimal publicTransportKm = BigDecimal.ZERO;
    private BigDecimal flightKm = BigDecimal.ZERO;
    private BigDecimal evEnergyKwh = BigDecimal.ZERO;

    // Energy & Residential
    private BigDecimal electricityKwh = BigDecimal.ZERO;
    private String gridRegion = "India National Grid";
    private BigDecimal lpgCylindersOrKg = BigDecimal.ZERO;
    private BigDecimal naturalGasKwh = BigDecimal.ZERO;

    // Agriculture & Food
    private String dietType = "MIXED"; // MEAT_HEAVY_DIET, MEDIUM_MEAT_DIET, VEGETARIAN_DIET, VEGAN_DIET
    private Integer meatServingsPerWeek = 4;
    private Integer dairyServingsPerWeek = 7;
    private BigDecimal foodWasteKg = new BigDecimal("2.00");

    // Shopping
    private Integer clothingItemsBought = 2;
    private Integer electronicsBought = 0;
    private BigDecimal generalGoodsSpendUsd = new BigDecimal("50.00");

    // Waste
    private BigDecimal wasteGeneratedKg = new BigDecimal("15.00");
    private BigDecimal recyclingPercentage = new BigDecimal("20.00");
    private Boolean compostingActive = false;

    // Industrial / Organizational Option
    private Boolean isIndustrial = false;
    private BigDecimal industrialEnergyKwh = BigDecimal.ZERO;
    private BigDecimal industrialFuelLitres = BigDecimal.ZERO;

    public ActivityInputDto() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getReportingPeriod() { return reportingPeriod; }
    public void setReportingPeriod(String reportingPeriod) { this.reportingPeriod = reportingPeriod; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }

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
}
