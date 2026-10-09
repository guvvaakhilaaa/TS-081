package com.ecoimpact.controller;

import com.ecoimpact.dto.GoalRequestDto;
import com.ecoimpact.model.ReductionGoal;
import com.ecoimpact.model.ReductionPlan;
import com.ecoimpact.service.ReductionPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GoalAndPlanController {

    private final ReductionPlanService reductionPlanService;

    public GoalAndPlanController(ReductionPlanService reductionPlanService) {
        this.reductionPlanService = reductionPlanService;
    }

    @PostMapping("/goals")
    public ResponseEntity<Map<String, Object>> createGoalAndPlan(@RequestBody GoalRequestDto request) {
        Map<String, Object> result = reductionPlanService.createGoalAndPlan(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/goals")
    public ResponseEntity<List<ReductionGoal>> getGoals(@RequestParam(defaultValue = "1") Long userId) {
        return ResponseEntity.ok(reductionPlanService.getUserGoals(userId));
    }

    @GetMapping("/plans")
    public ResponseEntity<List<ReductionPlan>> getPlans(@RequestParam(defaultValue = "1") Long userId) {
        return ResponseEntity.ok(reductionPlanService.getUserPlans(userId));
    }
}
