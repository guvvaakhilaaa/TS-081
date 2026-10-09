package com.ecoimpact;

import com.ecoimpact.model.Mission;
import com.ecoimpact.model.User;
import com.ecoimpact.model.UserMission;
import com.ecoimpact.repository.*;
import com.ecoimpact.service.GamificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GamificationServiceTest {

    private UserRepository userRepository;
    private MissionRepository missionRepository;
    private UserMissionRepository userMissionRepository;
    private AchievementRepository achievementRepository;
    private UserAchievementRepository userAchievementRepository;
    private XpTransactionRepository xpTransactionRepository;
    private GamificationService gamificationService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        missionRepository = Mockito.mock(MissionRepository.class);
        userMissionRepository = Mockito.mock(UserMissionRepository.class);
        achievementRepository = Mockito.mock(AchievementRepository.class);
        userAchievementRepository = Mockito.mock(UserAchievementRepository.class);
        xpTransactionRepository = Mockito.mock(XpTransactionRepository.class);

        gamificationService = new GamificationService(
                userRepository, missionRepository, userMissionRepository,
                achievementRepository, userAchievementRepository, xpTransactionRepository
        );
    }

    @Test
    void testCompleteMissionAwardsXpAndPreventsDuplicateClaim() {
        User user = new User();
        user.setId(1L);
        user.setEcoXp(100);
        user.setLevel(1);

        Mission mission = new Mission();
        mission.setId(5L);
        mission.setTitle("Meat-Free Monday");
        mission.setXpReward(50);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(missionRepository.findById(5L)).thenReturn(Optional.of(mission));
        when(userMissionRepository.findByUserIdAndMissionId(1L, 5L)).thenReturn(Optional.empty());

        // First completion
        Map<String, Object> result1 = gamificationService.completeMission(1L, 5L);
        assertTrue((Boolean) result1.get("success"));
        assertEquals(50, result1.get("awardedXp"));
        assertEquals(150, user.getEcoXp());
        // Level threshold: 150 XP promotes to Level 2 ("Sprout")
        assertEquals(2, user.getLevel());
        assertEquals("Sprout", user.getEcoRank());

        // Verify duplicate claim prevention
        UserMission claimedUm = new UserMission(user, mission);
        claimedUm.setCompleted(true);
        claimedUm.setClaimStatus("CLAIMED");
        when(userMissionRepository.findByUserIdAndMissionId(1L, 5L)).thenReturn(Optional.of(claimedUm));

        Map<String, Object> result2 = gamificationService.completeMission(1L, 5L);
        assertFalse((Boolean) result2.get("success"), "Duplicate claim must be prevented");
    }
}
