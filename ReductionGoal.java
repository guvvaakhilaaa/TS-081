package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reduction_goals")
public class ReductionGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "plan_id")
    private Long planId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "target_percentage", nullable = false, precision = 6, scale = 2)
    private BigDecimal targetPercentage;

    @Column(name = "target_kg_reduction", nullable = false, precision = 10, scale = 2)
    private BigDecimal targetKgReduction;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(name = "current_progress_percentage", precision = 6, scale = 2)
    private BigDecimal currentProgressPercentage = BigDecimal.ZERO;

    @Column(length = 30)
    private String status = "IN_PROGRESS"; // IN_PROGRESS, COMPLETED, PAUSED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ReductionGoal() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getTargetPercentage() { return targetPercentage; }
    public void setTargetPercentage(BigDecimal targetPercentage) { this.targetPercentage = targetPercentage; }

    public BigDecimal getTargetKgReduction() { return targetKgReduction; }
    public void setTargetKgReduction(BigDecimal targetKgReduction) { this.targetKgReduction = targetKgReduction; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public BigDecimal getCurrentProgressPercentage() { return currentProgressPercentage; }
    public void setCurrentProgressPercentage(BigDecimal currentProgressPercentage) { this.currentProgressPercentage = currentProgressPercentage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
