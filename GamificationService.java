package com.ecoimpact.service;

import com.ecoimpact.dto.GamificationProfileDto;
import com.ecoimpact.dto.LeaderboardEntryDto;
import com.ecoimpact.model.*;
import com.ecoimpact.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GamificationService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final XpTransactionRepository xpTransactionRepository;

    // Level XP Thresholds
    private static final int[] LEVEL_THRESHOLDS = {0, 150, 350, 650, 1000};
    private static final String[] ECO_RANKS = {"Seedling", "Sprout", "Green Guardian", "Eco Hero", "Planet Protector"};
    private static final String[] PLANET_STAGES = {
            "Stage 1: Seedling Islet (Initial clean plot)",
            "Stage 2: Sprouting Meadow (Lush grass & wild flora)",
            "Stage 3: Biodiverse Canopy (Flowering groves & clean stream)",
            "Stage 4: Thriving Eco-Sanctuary (Flourishing wildlife & clean wind energy)",
            "Stage 5: Harmonious Biosphere (Restored planetary equilibrium)"
    };

    public GamificationService(UserRepository userRepository,
                               MissionRepository missionRepository,
                               UserMissionRepository userMissionRepository,
                               AchievementRepository achievementRepository,
                               UserAchievementRepository userAchievementRepository,
                               XpTransactionRepository xpTransactionRepository) {
        this.userRepository = userRepository;
        this.missionRepository = missionRepository;
        this.userMissionRepository = userMissionRepository;
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.xpTransactionRepository = xpTransactionRepository;
    }

    /**
     * Algorithm 7: Gamification Algorithm - Profile Retrieval & Deterministic State Synchronization
     */
    @Transactional
    public GamificationProfileDto getGamificationProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // 1. Sync daily streak
        syncDailyStreak(user);

        // 2. Fetch or initialize missions
        List<Mission> allMissions = missionRepository.findByIsActiveTrue();
        List<UserMission> userMissions = userMissionRepository.findByUserId(userId);
        Map<Long, UserMission> umMap = new HashMap<>();
        for (UserMission um : userMissions) {
            umMap.put(um.getMission().getId(), um);
        }

        List<GamificationProfileDto.MissionStatusDto> missionDtos = new ArrayList<>();
        int completedMissions = 0;
        for (Mission m : allMissions) {
            UserMission um = umMap.get(m.getId());
            boolean isCompleted = (um != null && Boolean.TRUE.equals(um.getCompleted()));
            String claimStatus = (um != null && um.getClaimStatus() != null) ? um.getClaimStatus() : "PENDING";
            if (isCompleted) completedMissions++;

            missionDtos.add(new GamificationProfileDto.MissionStatusDto(
                    m.getId(), m.getTitle(), m.getDescription(), m.getCategory(),
                    m.getXpReward(), m.getFrequencyType(), m.getIconName(),
                    isCompleted, claimStatus
            ));
        }

        // 3. Fetch or initialize achievements
        List<Achievement> allAchievements = achievementRepository.findAll();
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        Set<Long> unlockedIds = new HashSet<>();
        for (UserAchievement ua : userAchievements) {
            unlockedIds.add(ua.getAchievement().getId());
        }

        List<GamificationProfileDto.BadgeStatusDto> badgeDtos = new ArrayList<>();
        for (Achievement a : allAchievements) {
            boolean isUnlocked = unlockedIds.contains(a.getId());
            badgeDtos.add(new GamificationProfileDto.BadgeStatusDto(
                    a.getId(), a.getBadgeKey(), a.getTitle(), a.getDescription(),
                    a.getXpBonus(), a.getIconName(), isUnlocked
            ));
        }

        // 4. Calculate Level, Rank, and Green Planet progression
        int currentXp = user.getEcoXp();
        int calculatedLevel = computeLevel(currentXp);
        String calculatedRank = ECO_RANKS[Math.min(calculatedLevel - 1, ECO_RANKS.length - 1)];

        user.setLevel(calculatedLevel);
        user.setEcoRank(calculatedRank);
        userRepository.save(user);

        int nextLevelXp = (calculatedLevel < LEVEL_THRESHOLDS.length) ? LEVEL_THRESHOLDS[calculatedLevel] : LEVEL_THRESHOLDS[LEVEL_THRESHOLDS.length - 1] + 500;
        int currentLevelBaseXp = LEVEL_THRESHOLDS[calculatedLevel - 1];
        int xpInLevel = Math.max(0, currentXp - currentLevelBaseXp);
        int span = Math.max(1, nextLevelXp - currentLevelBaseXp);
        int progressPct = Math.min(100, (xpInLevel * 100) / span);

        int planetStage = Math.min(5, Math.max(1, calculatedLevel));
        String planetTitle = PLANET_STAGES[planetStage - 1];

        // 5. Recent XP Transactions
        List<XpTransaction> recentTx = xpTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (recentTx.size() > 10) {
            recentTx = recentTx.subList(0, 10);
        }

        // 6. Assemble GamificationProfileDto
        GamificationProfileDto dto = new GamificationProfileDto();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEcoRank(user.getEcoRank());
        dto.setLevel(user.getLevel());
        dto.setEcoXp(user.getEcoXp());
        dto.setNextLevelXp(nextLevelXp);
        dto.setXpProgressToNextLevel(xpInLevel);
        dto.setXpProgressPercentage(progressPct);
        dto.setDailyStreak(user.getDailyStreak());
        dto.setGreenPlanetStage(planetStage);
        dto.setGreenPlanetTitle(planetTitle);
        dto.setCompletedMissionsCount(completedMissions);
        dto.setUnlockedBadgesCount(userAchievements.size());
        dto.setMissions(missionDtos);
        dto.setBadges(badgeDtos);
        dto.setRecentXpTransactions(recentTx);

        return dto;
    }

    /**
     * Complete an eligible mission with duplicate claim prevention
     */
    @Transactional
    public Map<String, Object> completeMission(Long userId, Long missionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found: " + missionId));

        Optional<UserMission> umOpt = userMissionRepository.findByUserIdAndMissionId(userId, missionId);
        UserMission um;
        if (umOpt.isPresent()) {
            um = umOpt.get();
            if ("CLAIMED".equalsIgnoreCase(um.getClaimStatus())) {
                Map<String, Object> err = new HashMap<>();
                err.put("success", false);
                err.put("message", "Mission reward has already been claimed.");
                return err;
            }
        } else {
            um = new UserMission(user, mission);
        }

        um.setCompleted(true);
        um.setCompletedAt(LocalDateTime.now());
        um.setClaimStatus("CLAIMED");
        userMissionRepository.save(um);

        // Award XP
        int xpReward = mission.getXpReward();
        user.setEcoXp(user.getEcoXp() + xpReward);
        user.setLevel(computeLevel(user.getEcoXp()));
        user.setEcoRank(ECO_RANKS[Math.min(user.getLevel() - 1, ECO_RANKS.length - 1)]);
        userRepository.save(user);

        // Record immutable audit transaction
        XpTransaction tx = new XpTransaction(user, xpReward, "MISSION", mission.getId(),
                "Completed mission: " + mission.getTitle());
        xpTransactionRepository.save(tx);

        // Check if milestone badges should be unlocked
        checkAndUnlockBadges(user);

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Mission completed! Earned +" + xpReward + " Eco XP");
        resp.put("awardedXp", xpReward);
        resp.put("newTotalXp", user.getEcoXp());
        resp.put("level", user.getLevel());
        resp.put("ecoRank", user.getEcoRank());
        return resp;
    }

    /**
     * Check and unlock achievements
     */
    @Transactional
    public void unlockAchievement(Long userId, String badgeKey) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        Optional<Achievement> achOpt = achievementRepository.findByBadgeKey(badgeKey);
        if (achOpt.isEmpty()) return;

        Achievement ach = achOpt.get();
        Optional<UserAchievement> uaOpt = userAchievementRepository.findByUserIdAndAchievementId(userId, ach.getId());
        if (uaOpt.isPresent()) return; // Already unlocked

        UserAchievement ua = new UserAchievement(user, ach);
        userAchievementRepository.save(ua);

        // Award achievement XP bonus
        int bonus = ach.getXpBonus();
        user.setEcoXp(user.getEcoXp() + bonus);
        user.setLevel(computeLevel(user.getEcoXp()));
        user.setEcoRank(ECO_RANKS[Math.min(user.getLevel() - 1, ECO_RANKS.length - 1)]);
        userRepository.save(user);

        XpTransaction tx = new XpTransaction(user, bonus, "ACHIEVEMENT", ach.getId(),
                "Unlocked badge: " + ach.getTitle());
        xpTransactionRepository.save(tx);
    }

    private void checkAndUnlockBadges(User user) {
        long missionCount = userMissionRepository.findByUserId(user.getId()).stream()
                .filter(um -> Boolean.TRUE.equals(um.getCompleted()))
                .count();

        if (missionCount >= 3) {
            unlockAchievement(user.getId(), "ECO_COMMUTER");
        }
        if (user.getDailyStreak() >= 7) {
            unlockAchievement(user.getId(), "SEVEN_DAY_STREAK");
        }
    }

    private void syncDailyStreak(User user) {
        LocalDate today = LocalDate.now();
        LocalDate lastActive = user.getLastActiveDate();

        if (lastActive == null) {
            user.setDailyStreak(1);
            user.setLastActiveDate(today);
            userRepository.save(user);
        } else if (lastActive.plusDays(1).isEqual(today)) {
            user.setDailyStreak(user.getDailyStreak() + 1);
            user.setLastActiveDate(today);
            userRepository.save(user);
        } else if (!lastActive.isEqual(today) && lastActive.plusDays(1).isBefore(today)) {
            // Streak reset after missed days
            user.setDailyStreak(1);
            user.setLastActiveDate(today);
            userRepository.save(user);
        }
    }

    private int computeLevel(int xp) {
        for (int i = LEVEL_THRESHOLDS.length - 1; i >= 0; i--) {
            if (xp >= LEVEL_THRESHOLDS[i]) {
                return i + 1;
            }
        }
        return 1;
    }

    public List<LeaderboardEntryDto> getLeaderboard() {
        List<User> topUsers = userRepository.findByOptInLeaderboardTrueOrderByEcoXpDesc();
        List<LeaderboardEntryDto> leaderboard = new ArrayList<>();
        int rank = 1;
        for (User u : topUsers) {
            leaderboard.add(new LeaderboardEntryDto(
                    rank++, u.getId(), u.getUsername(), u.getFullName(),
                    u.getCountry(), u.getEcoRank(), u.getLevel(), u.getEcoXp()
            ));
        }
        return leaderboard;
    }
}
