package com.ecoimpact.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarbonResultDto {
    private Long assessmentId;
    private Long userId;
    private LocalDate assessmentDate;
    private BigDecimal totalFootprintKg;
    private BigDecimal totalFootprintTonnes;
    private String largestCategory;
    private BigDecimal annualProjectionTonnes;
    private String reportingPeriod;
    private List<CategoryDetailDto> categories = new ArrayList<>();

    public CarbonResultDto() {}

    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(LocalDate assessmentDate) { this.assessmentDate = assessmentDate; }

    public BigDecimal getTotalFootprintKg() { return totalFootprintKg; }
    public void setTotalFootprintKg(BigDecimal totalFootprintKg) { this.totalFootprintKg = totalFootprintKg; }

    public BigDecimal getTotalFootprintTonnes() { return totalFootprintTonnes; }
    public void setTotalFootprintTonnes(BigDecimal totalFootprintTonnes) { this.totalFootprintTonnes = totalFootprintTonnes; }

    public String getLargestCategory() { return largestCategory; }
    public void setLargestCategory(String largestCategory) { this.largestCategory = largestCategory; }

    public BigDecimal getAnnualProjectionTonnes() { return annualProjectionTonnes; }
    public void setAnnualProjectionTonnes(BigDecimal annualProjectionTonnes) { this.annualProjectionTonnes = annualProjectionTonnes; }

    public String getReportingPeriod() { return reportingPeriod; }
    public void setReportingPeriod(String reportingPeriod) { this.reportingPeriod = reportingPeriod; }

    public List<CategoryDetailDto> getCategories() { return categories; }
    public void setCategories(List<CategoryDetailDto> categories) { this.categories = categories; }
}
