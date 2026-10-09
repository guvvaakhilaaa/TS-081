package com.ecoimpact.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SimulationResultDto {
    private String scenarioName;
    private BigDecimal baselineEmissionsKg;
    private BigDecimal proposedEmissionsKg;
    private BigDecimal reductionKg;
    private BigDecimal reductionPercentage;
    private BigDecimal annualAvoidedTonnes;
    private BigDecimal estimatedMonthlySavingsUsd;
    private List<CategoryComparisonDto> categoryComparisons = new ArrayList<>();
    private String simulationSummary;

    public static class CategoryComparisonDto {
        private String category;
        private BigDecimal baselineKg;
        private BigDecimal proposedKg;
        private BigDecimal reductionKg;
        private BigDecimal percentageChange;

        public CategoryComparisonDto() {}

        public CategoryComparisonDto(String category, BigDecimal baselineKg, BigDecimal proposedKg, BigDecimal reductionKg, BigDecimal percentageChange) {
            this.category = category;
            this.baselineKg = baselineKg;
            this.proposedKg = proposedKg;
            this.reductionKg = reductionKg;
            this.percentageChange = percentageChange;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public BigDecimal getBaselineKg() { return baselineKg; }
        public void setBaselineKg(BigDecimal baselineKg) { this.baselineKg = baselineKg; }

        public BigDecimal getProposedKg() { return proposedKg; }
        public void setProposedKg(BigDecimal proposedKg) { this.proposedKg = proposedKg; }

        public BigDecimal getReductionKg() { return reductionKg; }
        public void setReductionKg(BigDecimal reductionKg) { this.reductionKg = reductionKg; }

        public BigDecimal getPercentageChange() { return percentageChange; }
        public void setPercentageChange(BigDecimal percentageChange) { this.percentageChange = percentageChange; }
    }

    public SimulationResultDto() {}

    public String getScenarioName() { return scenarioName; }
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }

    public BigDecimal getBaselineEmissionsKg() { return baselineEmissionsKg; }
    public void setBaselineEmissionsKg(BigDecimal baselineEmissionsKg) { this.baselineEmissionsKg = baselineEmissionsKg; }

    public BigDecimal getProposedEmissionsKg() { return proposedEmissionsKg; }
    public void setProposedEmissionsKg(BigDecimal proposedEmissionsKg) { this.proposedEmissionsKg = proposedEmissionsKg; }

    public BigDecimal getReductionKg() { return reductionKg; }
    public void setReductionKg(BigDecimal reductionKg) { this.reductionKg = reductionKg; }

    public BigDecimal getReductionPercentage() { return reductionPercentage; }
    public void setReductionPercentage(BigDecimal reductionPercentage) { this.reductionPercentage = reductionPercentage; }

    public BigDecimal getAnnualAvoidedTonnes() { return annualAvoidedTonnes; }
    public void setAnnualAvoidedTonnes(BigDecimal annualAvoidedTonnes) { this.annualAvoidedTonnes = annualAvoidedTonnes; }

    public BigDecimal getEstimatedMonthlySavingsUsd() { return estimatedMonthlySavingsUsd; }
    public void setEstimatedMonthlySavingsUsd(BigDecimal estimatedMonthlySavingsUsd) { this.estimatedMonthlySavingsUsd = estimatedMonthlySavingsUsd; }

    public List<CategoryComparisonDto> getCategoryComparisons() { return categoryComparisons; }
    public void setCategoryComparisons(List<CategoryComparisonDto> categoryComparisons) { this.categoryComparisons = categoryComparisons; }

    public String getSimulationSummary() { return simulationSummary; }
    public void setSimulationSummary(String simulationSummary) { this.simulationSummary = simulationSummary; }
}
