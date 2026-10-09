package com.ecoimpact.controller;

import com.ecoimpact.service.ProgressTrackingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressTrackingService progressService;

    public ProgressController(ProgressTrackingService progressService) {
        this.progressService = progressService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProgressAnalytics(@RequestParam(defaultValue = "1") Long userId) {
        Map<String, Object> data = progressService.getProgressAnalytics(userId);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsvReport(@RequestParam(defaultValue = "1") Long userId) {
        String csvData = progressService.generateCsvReport(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ecoimpact_progress_report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData.getBytes());
    }
}
