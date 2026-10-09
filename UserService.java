package com.ecoimpact.service;

import com.ecoimpact.model.*;
import com.ecoimpact.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final ActivityRecordRepository activityRecordRepository;
    private final CarbonAssessmentRepository assessmentRepository;
    private final CategoryEmissionRepository categoryEmissionRepository;
    private final RecommendationRepository recommendationRepository;
    private final SimulationScenarioRepository simulationScenarioRepository;
    private final ReductionPlanRepository reductionPlanRepository;
    private final ReductionGoalRepository reductionGoalRepository;
    private final ActionRecordRepository actionRecordRepository;
    private final ProgressHistoryRepository progressHistoryRepository;
    private final SequestrationEstimateRepository sequestrationRepository;
    private final UserMissionRepository userMissionRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final XpTransactionRepository xpTransactionRepository;

    public UserService(UserRepository userRepository,
                       UserPreferenceRepository userPreferenceRepository,
                       ActivityRecordRepository activityRecordRepository,
                       CarbonAssessmentRepository assessmentRepository,
                       CategoryEmissionRepository categoryEmissionRepository,
                       RecommendationRepository recommendationRepository,
                       SimulationScenarioRepository simulationScenarioRepository,
                       ReductionPlanRepository reductionPlanRepository,
                       ReductionGoalRepository reductionGoalRepository,
                       ActionRecordRepository actionRecordRepository,
                       ProgressHistoryRepository progressHistoryRepository,
                       SequestrationEstimateRepository sequestrationRepository,
                       UserMissionRepository userMissionRepository,
                       UserAchievementRepository userAchievementRepository,
                       XpTransactionRepository xpTransactionRepository) {
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.activityRecordRepository = activityRecordRepository;
        this.assessmentRepository = assessmentRepository;
        this.categoryEmissionRepository = categoryEmissionRepository;
        this.recommendationRepository = recommendationRepository;
        this.simulationScenarioRepository = simulationScenarioRepository;
        this.reductionPlanRepository = reductionPlanRepository;
        this.reductionGoalRepository = reductionGoalRepository;
        this.actionRecordRepository = actionRecordRepository;
        this.progressHistoryRepository = progressHistoryRepository;
        this.sequestrationRepository = sequestrationRepository;
        this.userMissionRepository = userMissionRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.xpTransactionRepository = xpTransactionRepository;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    public Optional<UserPreference> getUserPreferences(Long userId) {
        return userPreferenceRepository.findByUserId(userId);
    }

    @Transactional
    public UserPreference savePreferences(Long userId, UserPreference pref) {
        User user = getUser(userId);
        UserPreference existing = userPreferenceRepository.findByUserId(userId).orElse(new UserPreference());
        existing.setUser(user);
        existing.setPrimaryTransport(pref.getPrimaryTransport());
        existing.setHouseholdSize(pref.getHouseholdSize());
        existing.setBudgetLevel(pref.getBudgetLevel());
        existing.setFeasibilityPreference(pref.getFeasibilityPreference());
        existing.setDietType(pref.getDietType());
        existing.setReceiveNotifications(pref.getReceiveNotifications());
        return userPreferenceRepository.save(existing);
    }

    /**
     * Resets demonstration data to baseline state for repeating hackathon presentations
     */
    @Transactional
    public void resetDemoData(Long userId) {
        User user = getUser(userId);
        user.setLevel(2);
        user.setEcoRank("Sprout");
        user.setEcoXp(185);
        user.setDailyStreak(4);
        user.setLastActiveDate(LocalDate.now());
        userRepository.save(user);

        // Delete simulation scenarios, goals, and reduction plans created during testing
        simulationScenarioRepository.deleteAll(simulationScenarioRepository.findByUserIdOrderByCreatedAtDesc(userId));
        reductionGoalRepository.deleteAll(reductionGoalRepository.findByUserIdOrderByDeadlineAsc(userId));
        reductionPlanRepository.deleteAll(reductionPlanRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }
}
