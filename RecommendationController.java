package com.ecoimpact.controller;

import com.ecoimpact.model.Recommendation;
import com.ecoimpact.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public ResponseEntity<List<Recommendation>> getRecommendations(@RequestParam(defaultValue = "1") Long userId) {
        List<Recommendation> list = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/generate")
    public ResponseEntity<List<Recommendation>> generateRecommendations(@RequestParam(defaultValue = "1") Long userId) {
        List<Recommendation> list = recommendationService.generateAndRankRecommendations(userId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Recommendation> updateRecommendationStatus(@PathVariable Long id,
                                                                     @RequestBody Map<String, String> body) {
        String status = body.getOrDefault("status", "ACCEPTED");
        Recommendation updated = recommendationService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}
