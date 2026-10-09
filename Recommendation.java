package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reasoning;

    @Column(name = "estimated_reduction_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedReductionKg;

    @Column(name = "potential_cost_savings_usd", precision = 10, scale = 2)
    private BigDecimal potentialCostSavingsUsd = BigDecimal.ZERO;

    @Column(name = "difficulty_level", length = 20)
    private String difficultyLevel = "MEDIUM";

    @Column(name = "implementation_time", length = 50)
    private String implementationTime = "1-2 weeks";

    @Column(name = "action_steps", columnDefinition = "TEXT")
    private String actionSteps;

    @Column(name = "ranking_score", nullable = false, precision = 8, scale = 4)
    private BigDecimal rankingScore;

    @Column(length = 30)
    private String status = "SUGGESTED"; // SUGGESTED, ACCEPTED, REJECTED, COMPLETED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Recommendation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }

    public BigDecimal getEstimatedReductionKg() { return estimatedReductionKg; }
    public void setEstimatedReductionKg(BigDecimal estimatedReductionKg) { this.estimatedReductionKg = estimatedReductionKg; }

    public BigDecimal getPotentialCostSavingsUsd() { return potentialCostSavingsUsd; }
    public void setPotentialCostSavingsUsd(BigDecimal potentialCostSavingsUsd) { this.potentialCostSavingsUsd = potentialCostSavingsUsd; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public String getImplementationTime() { return implementationTime; }
    public void setImplementationTime(String implementationTime) { this.implementationTime = implementationTime; }

    public String getActionSteps() { return actionSteps; }
    public void setActionSteps(String actionSteps) { this.actionSteps = actionSteps; }

    public BigDecimal getRankingScore() { return rankingScore; }
    public void setRankingScore(BigDecimal rankingScore) { this.rankingScore = rankingScore; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
