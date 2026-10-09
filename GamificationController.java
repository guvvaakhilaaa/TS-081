package com.ecoimpact.controller;

import com.ecoimpact.dto.GamificationProfileDto;
import com.ecoimpact.dto.LeaderboardEntryDto;
import com.ecoimpact.service.GamificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GamificationController {

    private final GamificationService gamificationService;

    public GamificationController(GamificationService gamificationService) {
        this.gamificationService = gamificationService;
    }

    @GetMapping("/gamification/profile")
    public ResponseEntity<GamificationProfileDto> getProfile(@RequestParam(defaultValue = "1") Long userId) {
        GamificationProfileDto profile = gamificationService.getGamificationProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/missions/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeMission(@PathVariable Long id,
                                                               @RequestParam(defaultValue = "1") Long userId) {
        Map<String, Object> result = gamificationService.completeMission(userId, id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard() {
        return ResponseEntity.ok(gamificationService.getLeaderboard());
    }
}
