package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_history")
public class ProgressHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "assessment_id")
    private Long assessmentId;

    @Column(name = "period_label", nullable = false, length = 50)
    private String periodLabel;

    @Column(name = "total_footprint_kg", nullable = false, precision = 12, scale = 4)
    private BigDecimal totalFootprintKg;

    @Column(name = "transport_kg", precision = 10, scale = 2)
    private BigDecimal transportKg = BigDecimal.ZERO;

    @Column(name = "energy_kg", precision = 10, scale = 2)
    private BigDecimal energyKg = BigDecimal.ZERO;

    @Column(name = "food_kg", precision = 10, scale = 2)
    private BigDecimal foodKg = BigDecimal.ZERO;

    @Column(name = "waste_kg", precision = 10, scale = 2)
    private BigDecimal wasteKg = BigDecimal.ZERO;

    @Column(name = "shopping_kg", precision = 10, scale = 2)
    private BigDecimal shoppingKg = BigDecimal.ZERO;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProgressHistory() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }

    public String getPeriodLabel() { return periodLabel; }
    public void setPeriodLabel(String periodLabel) { this.periodLabel = periodLabel; }

    public BigDecimal getTotalFootprintKg() { return totalFootprintKg; }
    public void setTotalFootprintKg(BigDecimal totalFootprintKg) { this.totalFootprintKg = totalFootprintKg; }

    public BigDecimal getTransportKg() { return transportKg; }
    public void setTransportKg(BigDecimal transportKg) { this.transportKg = transportKg; }

    public BigDecimal getEnergyKg() { return energyKg; }
    public void setEnergyKg(BigDecimal energyKg) { this.energyKg = energyKg; }

    public BigDecimal getFoodKg() { return foodKg; }
    public void setFoodKg(BigDecimal foodKg) { this.foodKg = foodKg; }

    public BigDecimal getWasteKg() { return wasteKg; }
    public void setWasteKg(BigDecimal wasteKg) { this.wasteKg = wasteKg; }

    public BigDecimal getShoppingKg() { return shoppingKg; }
    public void setShoppingKg(BigDecimal shoppingKg) { this.shoppingKg = shoppingKg; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
