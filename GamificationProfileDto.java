package com.ecoimpact.dto;

import com.ecoimpact.model.Achievement;
import com.ecoimpact.model.Mission;
import com.ecoimpact.model.XpTransaction;

import java.util.ArrayList;
import java.util.List;

public class GamificationProfileDto {
    private Long userId;
    private String username;
    private String fullName;
    private String ecoRank; // Seedling, Sprout, Green Guardian, Eco Hero, Planet Protector
    private Integer level;
    private Integer ecoXp;
    private Integer nextLevelXp;
    private Integer xpProgressToNextLevel;
    private Integer xpProgressPercentage;
    private Integer dailyStreak;
    private Integer greenPlanetStage; // 1 to 5
    private String greenPlanetTitle;
    private Integer completedMissionsCount;
    private Integer unlockedBadgesCount;

    private List<MissionStatusDto> missions = new ArrayList<>();
    private List<BadgeStatusDto> badges = new ArrayList<>();
    private List<XpTransaction> recentXpTransactions = new ArrayList<>();

    public static class MissionStatusDto {
        private Long missionId;
        private String title;
        private String description;
        private String category;
        private Integer xpReward;
        private String frequencyType;
        private String iconName;
        private Boolean completed;
        private String claimStatus;

        public MissionStatusDto() {}

        public MissionStatusDto(Long missionId, String title, String description, String category, Integer xpReward, String frequencyType, String iconName, Boolean completed, String claimStatus) {
            this.missionId = missionId;
            this.title = title;
            this.description = description;
            this.category = category;
            this.xpReward = xpReward;
            this.frequencyType = frequencyType;
            this.iconName = iconName;
            this.completed = completed;
            this.claimStatus = claimStatus;
        }

        public Long getMissionId() { return missionId; }
        public void setMissionId(Long missionId) { this.missionId = missionId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public Integer getXpReward() { return xpReward; }
        public void setXpReward(Integer xpReward) { this.xpReward = xpReward; }

        public String getFrequencyType() { return frequencyType; }
        public void setFrequencyType(String frequencyType) { this.frequencyType = frequencyType; }

        public String getIconName() { return iconName; }
        public void setIconName(String iconName) { this.iconName = iconName; }

        public Boolean getCompleted() { return completed; }
        public void setCompleted(Boolean completed) { this.completed = completed; }

        public String getClaimStatus() { return claimStatus; }
        public void setClaimStatus(String claimStatus) { this.claimStatus = claimStatus; }
    }

    public static class BadgeStatusDto {
        private Long achievementId;
        private String badgeKey;
        private String title;
        private String description;
        private Integer xpBonus;
        private String iconName;
        private Boolean unlocked;

        public BadgeStatusDto() {}

        public BadgeStatusDto(Long achievementId, String badgeKey, String title, String description, Integer xpBonus, String iconName, Boolean unlocked) {
            this.achievementId = achievementId;
            this.badgeKey = badgeKey;
            this.title = title;
            this.description = description;
            this.xpBonus = xpBonus;
            this.iconName = iconName;
            this.unlocked = unlocked;
        }

        public Long getAchievementId() { return achievementId; }
        public void setAchievementId(Long achievementId) { this.achievementId = achievementId; }

        public String getBadgeKey() { return badgeKey; }
        public void setBadgeKey(String badgeKey) { this.badgeKey = badgeKey; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Integer getXpBonus() { return xpBonus; }
        public void setXpBonus(Integer xpBonus) { this.xpBonus = xpBonus; }

        public String getIconName() { return iconName; }
        public void setIconName(String iconName) { this.iconName = iconName; }

        public Boolean getUnlocked() { return unlocked; }
        public void setUnlocked(Boolean unlocked) { this.unlocked = unlocked; }
    }

    public GamificationProfileDto() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEcoRank() { return ecoRank; }
    public void setEcoRank(String ecoRank) { this.ecoRank = ecoRank; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getEcoXp() { return ecoXp; }
    public void setEcoXp(Integer ecoXp) { this.ecoXp = ecoXp; }

    public Integer getNextLevelXp() { return nextLevelXp; }
    public void setNextLevelXp(Integer nextLevelXp) { this.nextLevelXp = nextLevelXp; }

    public Integer getXpProgressToNextLevel() { return xpProgressToNextLevel; }
    public void setXpProgressToNextLevel(Integer xpProgressToNextLevel) { this.xpProgressToNextLevel = xpProgressToNextLevel; }

    public Integer getXpProgressPercentage() { return xpProgressPercentage; }
    public void setXpProgressPercentage(Integer xpProgressPercentage) { this.xpProgressPercentage = xpProgressPercentage; }

    public Integer getDailyStreak() { return dailyStreak; }
    public void setDailyStreak(Integer dailyStreak) { this.dailyStreak = dailyStreak; }

    public Integer getGreenPlanetStage() { return greenPlanetStage; }
    public void setGreenPlanetStage(Integer greenPlanetStage) { this.greenPlanetStage = greenPlanetStage; }

    public String getGreenPlanetTitle() { return greenPlanetTitle; }
    public void setGreenPlanetTitle(String greenPlanetTitle) { this.greenPlanetTitle = greenPlanetTitle; }

    public Integer getCompletedMissionsCount() { return completedMissionsCount; }
    public void setCompletedMissionsCount(Integer completedMissionsCount) { this.completedMissionsCount = completedMissionsCount; }

    public Integer getUnlockedBadgesCount() { return unlockedBadgesCount; }
    public void setUnlockedBadgesCount(Integer unlockedBadgesCount) { this.unlockedBadgesCount = unlockedBadgesCount; }

    public List<MissionStatusDto> getMissions() { return missions; }
    public void setMissions(List<MissionStatusDto> missions) { this.missions = missions; }

    public List<BadgeStatusDto> getBadges() { return badges; }
    public void setBadges(List<BadgeStatusDto> badges) { this.badges = badges; }

    public List<XpTransaction> getRecentXpTransactions() { return recentXpTransactions; }
    public void setRecentXpTransactions(List<XpTransaction> recentXpTransactions) { this.recentXpTransactions = recentXpTransactions; }
}
