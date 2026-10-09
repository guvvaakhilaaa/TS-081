package com.ecoimpact.dto;

import java.math.BigDecimal;

public class SimulationRequestDto {
    private Long userId = 1L;
    private String scenarioName = "Smart Lifestyle Shift";

    // Modifications to baseline
    // 1. Transportation
    private Integer publicTransitDaysPerWeek = 2; // e.g., shift 2 driving days to bus/train
    private BigDecimal transportReductionPercent = new BigDecimal("30.00");
    private String alternativeTransportMode = "CITY_BUS";

    // 2. Residential
    private BigDecimal electricityReductionPercent = new BigDecimal("20.00"); // e.g. LED bulbs, AC temperature 24C

    // 3. Food
    private String proposedDietType = "VEGETARIAN_DIET"; // or VEGAN_DIET
    private BigDecimal foodWasteReductionPercent = new BigDecimal("50.00");

    // 4. Waste
    private BigDecimal proposedRecyclingPercent = new BigDecimal("60.00");
    private Boolean enableComposting = true;

    // 5. Shopping
    private BigDecimal shoppingReductionPercent = new BigDecimal("25.00");

    public SimulationRequestDto() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getScenarioName() { return scenarioName; }
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }

    public Integer getPublicTransitDaysPerWeek() { return publicTransitDaysPerWeek; }
    public void setPublicTransitDaysPerWeek(Integer publicTransitDaysPerWeek) { this.publicTransitDaysPerWeek = publicTransitDaysPerWeek; }

    public BigDecimal getTransportReductionPercent() { return transportReductionPercent; }
    public void setTransportReductionPercent(BigDecimal transportReductionPercent) { this.transportReductionPercent = transportReductionPercent; }

    public String getAlternativeTransportMode() { return alternativeTransportMode; }
    public void setAlternativeTransportMode(String alternativeTransportMode) { this.alternativeTransportMode = alternativeTransportMode; }

    public BigDecimal getElectricityReductionPercent() { return electricityReductionPercent; }
    public void setElectricityReductionPercent(BigDecimal electricityReductionPercent) { this.electricityReductionPercent = electricityReductionPercent; }

    public String getProposedDietType() { return proposedDietType; }
    public void setProposedDietType(String proposedDietType) { this.proposedDietType = proposedDietType; }

    public BigDecimal getFoodWasteReductionPercent() { return foodWasteReductionPercent; }
    public void setFoodWasteReductionPercent(BigDecimal foodWasteReductionPercent) { this.foodWasteReductionPercent = foodWasteReductionPercent; }

    public BigDecimal getProposedRecyclingPercent() { return proposedRecyclingPercent; }
    public void setProposedRecyclingPercent(BigDecimal proposedRecyclingPercent) { this.proposedRecyclingPercent = proposedRecyclingPercent; }

    public Boolean getEnableComposting() { return enableComposting; }
    public void setEnableComposting(Boolean enableComposting) { this.enableComposting = enableComposting; }

    public BigDecimal getShoppingReductionPercent() { return shoppingReductionPercent; }
    public void setShoppingReductionPercent(BigDecimal shoppingReductionPercent) { this.shoppingReductionPercent = shoppingReductionPercent; }
}
