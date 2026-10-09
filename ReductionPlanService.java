package com.ecoimpact.service;

import com.ecoimpact.dto.GoalRequestDto;
import com.ecoimpact.model.*;
import com.ecoimpact.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class ReductionPlanService {

    private final ReductionPlanRepository planRepository;
    private final ReductionGoalRepository goalRepository;
    private final CarbonAssessmentRepository assessmentRepository;
    private final RecommendationRepository recommendationRepository;
    private final ActionRecordRepository actionRecordRepository;
    private final UserRepository userRepository;

    public ReductionPlanService(ReductionPlanRepository planRepository,
                                ReductionGoalRepository goalRepository,
                                CarbonAssessmentRepository assessmentRepository,
                                RecommendationRepository recommendationRepository,
                                ActionRecordRepository actionRecordRepository,
                                UserRepository userRepository) {
        this.planRepository = planRepository;
        this.goalRepository = goalRepository;
        this.assessmentRepository = assessmentRepository;
        this.recommendationRepository = recommendationRepository;
        this.actionRecordRepository = actionRecordRepository;
        this.userRepository = userRepository;
    }

    /**
     * Algorithm 5: Goal Planning Algorithm
     */
    @Transactional
    public Map<String, Object> createGoalAndPlan(GoalRequestDto req) {
        User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        Optional<CarbonAssessment> assessmentOpt = assessmentRepository.findFirstByUserIdOrderByAssessmentDateDesc(user.getId());
        BigDecimal baseline = assessmentOpt.map(CarbonAssessment::getTotalFootprintKg).orElse(new BigDecimal("500.00"));

        BigDecimal targetPct = req.getTargetPercentage() != null ? req.getTargetPercentage() : new BigDecimal("20.00");
        Integer duration = req.getDurationMonths() != null ? req.getDurationMonths() : 6;

        // Target Emissions = Baseline Emissions × (1 − Target Percentage ÷ 100)
        BigDecimal targetReductionRatio = BigDecimal.ONE.subtract(targetPct.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        BigDecimal targetEmissions = baseline.multiply(targetReductionRatio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal targetKgReduction = baseline.subtract(targetEmissions).setScale(2, RoundingMode.HALF_UP);

        // 1. Create or Update Reduction Plan
        ReductionPlan plan = new ReductionPlan();
        plan.setUser(user);
        plan.setPlanTitle(req.getTitle() != null ? req.getTitle() : "Custom " + targetPct + "% Reduction Plan");
        plan.setBaselineEmissionsKg(baseline);
        plan.setTargetReductionPercentage(targetPct);
        plan.setTargetEmissionsKg(targetEmissions);
        plan.setDurationMonths(duration);
        plan.setStartDate(LocalDate.now());
        plan.setEndDate(LocalDate.now().plusMonths(duration));
        plan.setStatus("ACTIVE");

        ReductionPlan savedPlan = planRepository.save(plan);

        // 2. Create Reduction Goal
        ReductionGoal goal = new ReductionGoal();
        goal.setUser(user);
        goal.setPlanId(savedPlan.getId());
        goal.setTitle(req.getTitle() != null ? req.getTitle() : "Reduce Carbon by " + targetPct + "%");
        goal.setTargetPercentage(targetPct);
        goal.setTargetKgReduction(targetKgReduction);
        goal.setDeadline(LocalDate.now().plusMonths(duration));
        goal.setCurrentProgressPercentage(BigDecimal.ZERO);
        goal.setStatus("IN_PROGRESS");

        ReductionGoal savedGoal = goalRepository.save(goal);

        // 3. Generate Monthly Milestones
        List<Map<String, Object>> milestones = new ArrayList<>();
        BigDecimal monthlyIncrementPct = targetPct.divide(new BigDecimal(duration), 2, RoundingMode.HALF_UP);
        BigDecimal monthlyIncrementKg = targetKgReduction.divide(new BigDecimal(duration), 2, RoundingMode.HALF_UP);

        for (int m = 1; m <= duration; m++) {
            Map<String, Object> milestone = new HashMap<>();
            milestone.put("monthNumber", m);
            milestone.put("targetMonthDate", LocalDate.now().plusMonths(m));
            milestone.put("cumulativePercentageTarget", monthlyIncrementPct.multiply(new BigDecimal(m)).min(targetPct));
            milestone.put("cumulativeKgReductionTarget", monthlyIncrementKg.multiply(new BigDecimal(m)).min(targetKgReduction));
            
            // Assign focus area based on month
            String focus = switch (m) {
                case 1 -> "Household Energy Efficiency & Standby Power Elimination";
                case 2 -> "Modal Shift: Public Transit & Active Commuting";
                case 3 -> "Sustainable Nutrition: Plant-Rich Meal Planning";
                case 4 -> "Zero-Waste Habits: Composting & 60% Recycling";
                case 5 -> "Mindful Consumption & Slow Fashion";
                default -> "Consolidation, Verification & Annual Footprint Review";
            };
            milestone.put("strategicFocus", focus);
            milestones.add(milestone);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("plan", savedPlan);
        response.put("goal", savedGoal);
        response.put("baselineEmissionsKg", baseline);
        response.put("targetEmissionsKg", targetEmissions);
        response.put("targetKgReduction", targetKgReduction);
        response.put("targetPercentage", targetPct);
        response.put("milestones", milestones);

        return response;
    }

    public List<ReductionPlan> getUserPlans(Long userId) {
        return planRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<ReductionGoal> getUserGoals(Long userId) {
        return goalRepository.findByUserIdOrderByDeadlineAsc(userId);
    }
}
