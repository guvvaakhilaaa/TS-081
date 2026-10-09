package com.ecoimpact.controller;

import com.ecoimpact.model.User;
import com.ecoimpact.model.UserPreference;
import com.ecoimpact.service.MachineLearningClusteringService;
import com.ecoimpact.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final MachineLearningClusteringService mlService;

    public UserController(UserService userService, MachineLearningClusteringService mlService) {
        this.userService = userService;
        this.mlService = mlService;
    }

    @GetMapping("/user/profile")
    public ResponseEntity<User> getProfile(@RequestParam(defaultValue = "1") Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/user/preferences")
    public ResponseEntity<UserPreference> getPreferences(@RequestParam(defaultValue = "1") Long userId) {
        return ResponseEntity.ok(userService.getUserPreferences(userId).orElse(new UserPreference()));
    }

    @PostMapping("/user/preferences")
    public ResponseEntity<UserPreference> updatePreferences(@RequestParam(defaultValue = "1") Long userId,
                                                            @RequestBody UserPreference preference) {
        return ResponseEntity.ok(userService.savePreferences(userId, preference));
    }

    @PostMapping("/demo/reset")
    public ResponseEntity<Map<String, Object>> resetDemoData(@RequestParam(defaultValue = "1") Long userId) {
        userService.resetDemoData(userId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Demo environment reset to standard initial benchmark state."
        ));
    }

    @GetMapping("/ml/cluster")
    public ResponseEntity<Map<String, Object>> classifyEcoPersona(@RequestParam(defaultValue = "1") Long userId) {
        return ResponseEntity.ok(mlService.classifyUserEcoPersona(userId));
    }
}
