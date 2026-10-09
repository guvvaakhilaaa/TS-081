package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sequestration_estimates")
public class SequestrationEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "method_type", nullable = false, length = 50)
    private String methodType; // BIOLOGICAL_TREE, SOIL_ORGANIC, BLUE_CARBON, TECH_DAC

    @Column(name = "biomass_or_quantity", nullable = false, precision = 12, scale = 4)
    private BigDecimal biomassOrQuantity;

    @Column(name = "carbon_fraction", precision = 6, scale = 4)
    private BigDecimal carbonFraction = new BigDecimal("0.5000");

    @Column(name = "co2_stored_kg", nullable = false, precision = 12, scale = 4)
    private BigDecimal co2StoredKg;

    @Column(name = "uncertainty_percentage", precision = 5, scale = 2)
    private BigDecimal uncertaintyPercentage = new BigDecimal("15.00");

    @Column(name = "methodology_notes", columnDefinition = "TEXT")
    private String methodologyNotes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public SequestrationEstimate() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getMethodType() { return methodType; }
    public void setMethodType(String methodType) { this.methodType = methodType; }

    public BigDecimal getBiomassOrQuantity() { return biomassOrQuantity; }
    public void setBiomassOrQuantity(BigDecimal biomassOrQuantity) { this.biomassOrQuantity = biomassOrQuantity; }

    public BigDecimal getCarbonFraction() { return carbonFraction; }
    public void setCarbonFraction(BigDecimal carbonFraction) { this.carbonFraction = carbonFraction; }

    public BigDecimal getCo2StoredKg() { return co2StoredKg; }
    public void setCo2StoredKg(BigDecimal co2StoredKg) { this.co2StoredKg = co2StoredKg; }

    public BigDecimal getUncertaintyPercentage() { return uncertaintyPercentage; }
    public void setUncertaintyPercentage(BigDecimal uncertaintyPercentage) { this.uncertaintyPercentage = uncertaintyPercentage; }

    public String getMethodologyNotes() { return methodologyNotes; }
    public void setMethodologyNotes(String methodologyNotes) { this.methodologyNotes = methodologyNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
