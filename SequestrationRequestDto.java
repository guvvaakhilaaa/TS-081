package com.ecoimpact.dto;

import java.math.BigDecimal;

public class SequestrationRequestDto {
    private Long userId = 1L;
    private String methodType = "BIOLOGICAL_TREE"; // BIOLOGICAL_TREE, SOIL_ORGANIC, BLUE_CARBON, TECH_DAC
    private BigDecimal quantity = new BigDecimal("25.00"); // trees planted or biomass tons
    private BigDecimal treeAgeYears = new BigDecimal("5.00");
    private BigDecimal carbonFraction = new BigDecimal("0.5000");

    public SequestrationRequestDto() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMethodType() { return methodType; }
    public void setMethodType(String methodType) { this.methodType = methodType; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getTreeAgeYears() { return treeAgeYears; }
    public void setTreeAgeYears(BigDecimal treeAgeYears) { this.treeAgeYears = treeAgeYears; }

    public BigDecimal getCarbonFraction() { return carbonFraction; }
    public void setCarbonFraction(BigDecimal carbonFraction) { this.carbonFraction = carbonFraction; }
}
