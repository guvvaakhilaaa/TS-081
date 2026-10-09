package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulation_scenarios")
public class SimulationScenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "scenario_name", nullable = false, length = 150)
    private String scenarioName;

    @Column(name = "baseline_emissions_kg", nullable = false, precision = 12, scale = 4)
    private BigDecimal baselineEmissionsKg;

    @Column(name = "proposed_emissions_kg", nullable = false, precision = 12, scale = 4)
    private BigDecimal proposedEmissionsKg;

    @Column(name = "reduction_kg", nullable = false, precision = 12, scale = 4)
    private BigDecimal reductionKg;

    @Column(name = "reduction_percentage", nullable = false, precision = 6, scale = 2)
    private BigDecimal reductionPercentage;

    @Column(name = "changes_summary", columnDefinition = "TEXT")
    private String changesSummary;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public SimulationScenario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public String getChangesSummary() { return changesSummary; }
    public void setChangesSummary(String changesSummary) { this.changesSummary = changesSummary; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
