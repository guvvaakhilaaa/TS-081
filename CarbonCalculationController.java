package com.ecoimpact.controller;

import com.ecoimpact.dto.ActivityInputDto;
import com.ecoimpact.dto.CarbonResultDto;
import com.ecoimpact.model.CarbonAssessment;
import com.ecoimpact.model.EmissionFactor;
import com.ecoimpact.repository.CarbonAssessmentRepository;
import com.ecoimpact.service.CarbonCalculationService;
import com.ecoimpact.service.EmissionFactorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CarbonCalculationController {

    private final CarbonCalculationService calculationService;
    private final CarbonAssessmentRepository assessmentRepository;
    private final EmissionFactorService factorService;

    public CarbonCalculationController(CarbonCalculationService calculationService,
                                       CarbonAssessmentRepository assessmentRepository,
                                       EmissionFactorService factorService) {
        this.calculationService = calculationService;
        this.assessmentRepository = assessmentRepository;
        this.factorService = factorService;
    }

    @PostMapping("/activities")
    public ResponseEntity<CarbonResultDto> saveActivityAndCalculate(@RequestBody ActivityInputDto input) {
        CarbonResultDto result = calculationService.calculateAndSave(input);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/carbon/calculate")
    public ResponseEntity<CarbonResultDto> calculateFootprint(@RequestBody ActivityInputDto input) {
        CarbonResultDto result = calculationService.calculateAndSave(input);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/carbon/assessments")
    public ResponseEntity<List<CarbonAssessment>> getAssessmentHistory(@RequestParam(defaultValue = "1") Long userId) {
        List<CarbonAssessment> list = assessmentRepository.findByUserIdOrderByAssessmentDateDesc(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/factors")
    public ResponseEntity<List<EmissionFactor>> getAllFactors() {
        return ResponseEntity.ok(factorService.getAllFactors());
    }
}
