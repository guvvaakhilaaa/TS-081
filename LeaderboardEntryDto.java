package com.ecoimpact.dto;

public class LeaderboardEntryDto {
    private Integer rank;
    private Long userId;
    private String username;
    private String fullName;
    private String country;
    private String ecoRank;
    private Integer level;
    private Integer ecoXp;

    public LeaderboardEntryDto() {}

    public LeaderboardEntryDto(Integer rank, Long userId, String username, String fullName, String country, String ecoRank, Integer level, Integer ecoXp) {
        this.rank = rank;
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.country = country;
        this.ecoRank = ecoRank;
        this.level = level;
        this.ecoXp = ecoXp;
    }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getEcoRank() { return ecoRank; }
    public void setEcoRank(String ecoRank) { this.ecoRank = ecoRank; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getEcoXp() { return ecoXp; }
    public void setEcoXp(Integer ecoXp) { this.ecoXp = ecoXp; }
}
