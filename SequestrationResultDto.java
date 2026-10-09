package com.ecoimpact.dto;

import java.math.BigDecimal;

public class SequestrationResultDto {
    private String methodType;
    private BigDecimal inputQuantity;
    private BigDecimal carbonStoredKg;
    private BigDecimal co2StoredKg;
    private BigDecimal co2StoredTonnes;
    private BigDecimal uncertaintyPercentage;
    private String scientificFormula;
    private String methodologyExplanation;
    private String accountingCaution;

    public SequestrationResultDto() {}

    public String getMethodType() { return methodType; }
    public void setMethodType(String methodType) { this.methodType = methodType; }

    public BigDecimal getInputQuantity() { return inputQuantity; }
    public void setInputQuantity(BigDecimal inputQuantity) { this.inputQuantity = inputQuantity; }

    public BigDecimal getCarbonStoredKg() { return carbonStoredKg; }
    public void setCarbonStoredKg(BigDecimal carbonStoredKg) { this.carbonStoredKg = carbonStoredKg; }

    public BigDecimal getCo2StoredKg() { return co2StoredKg; }
    public void setCo2StoredKg(BigDecimal co2StoredKg) { this.co2StoredKg = co2StoredKg; }

    public BigDecimal getCo2StoredTonnes() { return co2StoredTonnes; }
    public void setCo2StoredTonnes(BigDecimal co2StoredTonnes) { this.co2StoredTonnes = co2StoredTonnes; }

    public BigDecimal getUncertaintyPercentage() { return uncertaintyPercentage; }
    public void setUncertaintyPercentage(BigDecimal uncertaintyPercentage) { this.uncertaintyPercentage = uncertaintyPercentage; }

    public String getScientificFormula() { return scientificFormula; }
    public void setScientificFormula(String scientificFormula) { this.scientificFormula = scientificFormula; }

    public String getMethodologyExplanation() { return methodologyExplanation; }
    public void setMethodologyExplanation(String methodologyExplanation) { this.methodologyExplanation = methodologyExplanation; }

    public String getAccountingCaution() { return accountingCaution; }
    public void setAccountingCaution(String accountingCaution) { this.accountingCaution = accountingCaution; }
}
