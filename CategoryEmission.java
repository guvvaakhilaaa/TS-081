package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "category_emissions")
public class CategoryEmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    @JsonBackReference
    private CarbonAssessment carbonAssessment;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "emissions_kg", nullable = false, precision = 12, scale = 4)
    private BigDecimal emissionsKg;

    @Column(name = "percentage_contribution", nullable = false, precision = 6, scale = 2)
    private BigDecimal percentageContribution;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public CategoryEmission() {}

    public CategoryEmission(CarbonAssessment carbonAssessment, String category, BigDecimal emissionsKg, BigDecimal percentageContribution) {
        this.carbonAssessment = carbonAssessment;
        this.category = category;
        this.emissionsKg = emissionsKg;
        this.percentageContribution = percentageContribution;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CarbonAssessment getCarbonAssessment() { return carbonAssessment; }
    public void setCarbonAssessment(CarbonAssessment carbonAssessment) { this.carbonAssessment = carbonAssessment; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getEmissionsKg() { return emissionsKg; }
    public void setEmissionsKg(BigDecimal emissionsKg) { this.emissionsKg = emissionsKg; }

    public BigDecimal getPercentageContribution() { return percentageContribution; }
    public void setPercentageContribution(BigDecimal percentageContribution) { this.percentageContribution = percentageContribution; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
