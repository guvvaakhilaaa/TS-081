package com.ecoimpact.controller;

import com.ecoimpact.dto.HotspotResultDto;
import com.ecoimpact.service.HotspotAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emissions")
public class EmissionHotspotController {

    private final HotspotAnalysisService hotspotService;

    public EmissionHotspotController(HotspotAnalysisService hotspotService) {
        this.hotspotService = hotspotService;
    }

    @GetMapping("/hotspots")
    public ResponseEntity<HotspotResultDto> getEmissionHotspots(@RequestParam(defaultValue = "1") Long userId) {
        HotspotResultDto dto = hotspotService.detectHotspots(userId);
        return ResponseEntity.ok(dto);
    }
}
