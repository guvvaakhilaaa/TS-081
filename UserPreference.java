package com.ecoimpact.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "primary_transport")
    private String primaryTransport = "PETROL_CAR";

    @Column(name = "household_size")
    private Integer householdSize = 3;

    @Column(name = "budget_level")
    private String budgetLevel = "MEDIUM";

    @Column(name = "feasibility_preference")
    private String feasibilityPreference = "HIGH";

    @Column(name = "diet_type")
    private String dietType = "MIXED";

    @Column(name = "receive_notifications")
    private Boolean receiveNotifications = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UserPreference() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPrimaryTransport() { return primaryTransport; }
    public void setPrimaryTransport(String primaryTransport) { this.primaryTransport = primaryTransport; }

    public Integer getHouseholdSize() { return householdSize; }
    public void setHouseholdSize(Integer householdSize) { this.householdSize = householdSize; }

    public String getBudgetLevel() { return budgetLevel; }
    public void setBudgetLevel(String budgetLevel) { this.budgetLevel = budgetLevel; }

    public String getFeasibilityPreference() { return feasibilityPreference; }
    public void setFeasibilityPreference(String feasibilityPreference) { this.feasibilityPreference = feasibilityPreference; }

    public String getDietType() { return dietType; }
    public void setDietType(String dietType) { this.dietType = dietType; }

    public Boolean getReceiveNotifications() { return receiveNotifications; }
    public void setReceiveNotifications(Boolean receiveNotifications) { this.receiveNotifications = receiveNotifications; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
