package com.ecoimpact.dto;

import java.math.BigDecimal;

public class CategoryDetailDto {
    private String category;
    private BigDecimal emissionsKg;
    private BigDecimal percentageContribution;
    private String formulaApplied;
    private String keyFactorDescription;

    public CategoryDetailDto() {}

    public CategoryDetailDto(String category, BigDecimal emissionsKg, BigDecimal percentageContribution, String formulaApplied, String keyFactorDescription) {
        this.category = category;
        this.emissionsKg = emissionsKg;
        this.percentageContribution = percentageContribution;
        this.formulaApplied = formulaApplied;
        this.keyFactorDescription = keyFactorDescription;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getEmissionsKg() { return emissionsKg; }
    public void setEmissionsKg(BigDecimal emissionsKg) { this.emissionsKg = emissionsKg; }

    public BigDecimal getPercentageContribution() { return percentageContribution; }
    public void setPercentageContribution(BigDecimal percentageContribution) { this.percentageContribution = percentageContribution; }

    public String getFormulaApplied() { return formulaApplied; }
    public void setFormulaApplied(String formulaApplied) { this.formulaApplied = formulaApplied; }

    public String getKeyFactorDescription() { return keyFactorDescription; }
    public void setKeyFactorDescription(String keyFactorDescription) { this.keyFactorDescription = keyFactorDescription; }
}
