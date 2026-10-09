package com.ecoimpact.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "emission_factors")
public class EmissionFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "activity_type", nullable = false, length = 100)
    private String activityType;

    @Column(name = "factor_value", nullable = false, precision = 10, scale = 4)
    private BigDecimal factorValue;

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(name = "country_region", nullable = false, length = 100)
    private String countryRegion;

    @Column(name = "source_reference", nullable = false)
    private String sourceReference;

    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @Column(name = "calculation_boundary", nullable = false, length = 100)
    private String calculationBoundary;

    private String version = "v2024.1";

    @Column(columnDefinition = "TEXT")
    private String assumptions;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public EmissionFactor() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }

    public BigDecimal getFactorValue() { return factorValue; }
    public void setFactorValue(BigDecimal factorValue) { this.factorValue = factorValue; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getCountryRegion() { return countryRegion; }
    public void setCountryRegion(String countryRegion) { this.countryRegion = countryRegion; }

    public String getSourceReference() { return sourceReference; }
    public void setSourceReference(String sourceReference) { this.sourceReference = sourceReference; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public String getCalculationBoundary() { return calculationBoundary; }
    public void setCalculationBoundary(String calculationBoundary) { this.calculationBoundary = calculationBoundary; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getAssumptions() { return assumptions; }
    public void setAssumptions(String assumptions) { this.assumptions = assumptions; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
