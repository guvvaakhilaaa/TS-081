package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reduction_plans")
public class ReductionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "plan_title", nullable = false, length = 150)
    private String planTitle;

    @Column(name = "baseline_emissions_kg", nullable = false, precision = 12, scale = 4)
    private BigDecimal baselineEmissionsKg;

    @Column(name = "target_reduction_percentage", nullable = false, precision = 6, scale = 2)
    private BigDecimal targetReductionPercentage;

    @Column(name = "target_emissions_kg", nullable = false, precision = 12, scale = 4)
    private BigDecimal targetEmissionsKg;

    @Column(name = "duration_months")
    private Integer durationMonths = 6;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate = LocalDate.now();

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate = LocalDate.now().plusMonths(6);

    @Column(length = 30)
    private String status = "ACTIVE"; // ACTIVE, PAUSED, COMPLETED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ReductionPlan() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPlanTitle() { return planTitle; }
    public void setPlanTitle(String planTitle) { this.planTitle = planTitle; }

    public BigDecimal getBaselineEmissionsKg() { return baselineEmissionsKg; }
    public void setBaselineEmissionsKg(BigDecimal baselineEmissionsKg) { this.baselineEmissionsKg = baselineEmissionsKg; }

    public BigDecimal getTargetReductionPercentage() { return targetReductionPercentage; }
    public void setTargetReductionPercentage(BigDecimal targetReductionPercentage) { this.targetReductionPercentage = targetReductionPercentage; }

    public BigDecimal getTargetEmissionsKg() { return targetEmissionsKg; }
    public void setTargetEmissionsKg(BigDecimal targetEmissionsKg) { this.targetEmissionsKg = targetEmissionsKg; }

    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
