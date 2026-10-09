package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carbon_assessments")
public class CarbonAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "activity_record_id")
    private Long activityRecordId;

    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate = LocalDate.now();

    @Column(name = "total_footprint_kg", nullable = false, precision = 12, scale = 4)
    private BigDecimal totalFootprintKg;

    @Column(name = "total_footprint_tonnes", nullable = false, precision = 10, scale = 4)
    private BigDecimal totalFootprintTonnes;

    @Column(name = "largest_category", length = 50)
    private String largestCategory;

    @Column(name = "annual_projection_tonnes", nullable = false, precision = 10, scale = 4)
    private BigDecimal annualProjectionTonnes;

    @Column(name = "reporting_period")
    private String reportingPeriod = "MONTHLY";

    @OneToMany(mappedBy = "carbonAssessment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CategoryEmission> categoryEmissions = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public CarbonAssessment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Long getActivityRecordId() { return activityRecordId; }
    public void setActivityRecordId(Long activityRecordId) { this.activityRecordId = activityRecordId; }

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

    public List<CategoryEmission> getCategoryEmissions() { return categoryEmissions; }
    public void setCategoryEmissions(List<CategoryEmission> categoryEmissions) { this.categoryEmissions = categoryEmissions; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
