package com.ecoimpact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    private String country = "India";

    @Column(name = "region_grid")
    private String regionGrid = "India National Grid";

    @Column(name = "eco_rank")
    private String ecoRank = "Seedling";

    private Integer level = 1;

    @Column(name = "eco_xp")
    private Integer ecoXp = 0;

    @Column(name = "daily_streak")
    private Integer dailyStreak = 1;

    @Column(name = "last_active_date")
    private LocalDate lastActiveDate = LocalDate.now();

    @Column(name = "opt_in_leaderboard")
    private Boolean optInLeaderboard = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public User(String username, String email, String passwordHash, String fullName) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getRegionGrid() { return regionGrid; }
    public void setRegionGrid(String regionGrid) { this.regionGrid = regionGrid; }

    public String getEcoRank() { return ecoRank; }
    public void setEcoRank(String ecoRank) { this.ecoRank = ecoRank; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getEcoXp() { return ecoXp; }
    public void setEcoXp(Integer ecoXp) { this.ecoXp = ecoXp; }

    public Integer getDailyStreak() { return dailyStreak; }
    public void setDailyStreak(Integer dailyStreak) { this.dailyStreak = dailyStreak; }

    public LocalDate getLastActiveDate() { return lastActiveDate; }
    public void setLastActiveDate(LocalDate lastActiveDate) { this.lastActiveDate = lastActiveDate; }

    public Boolean getOptInLeaderboard() { return optInLeaderboard; }
    public void setOptInLeaderboard(Boolean optInLeaderboard) { this.optInLeaderboard = optInLeaderboard; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
