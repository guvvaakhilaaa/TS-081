package com.ecoimpact.controller;

import com.ecoimpact.dto.SequestrationRequestDto;
import com.ecoimpact.dto.SequestrationResultDto;
import com.ecoimpact.model.SequestrationEstimate;
import com.ecoimpact.service.SequestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sequestration")
public class SequestrationController {

    private final SequestrationService sequestrationService;

    public SequestrationController(SequestrationService sequestrationService) {
        this.sequestrationService = sequestrationService;
    }

    @GetMapping
    public ResponseEntity<List<SequestrationEstimate>> getEstimates(@RequestParam(defaultValue = "1") Long userId) {
        return ResponseEntity.ok(sequestrationService.getUserEstimates(userId));
    }

    @PostMapping("/calculate")
    public ResponseEntity<SequestrationResultDto> calculateSequestration(@RequestBody SequestrationRequestDto request) {
        SequestrationResultDto result = sequestrationService.calculateAndSave(request);
        return ResponseEntity.ok(result);
    }
}
