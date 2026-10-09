package com.ecoimpact.controller;

import com.ecoimpact.dto.SimulationRequestDto;
import com.ecoimpact.dto.SimulationResultDto;
import com.ecoimpact.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulations")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping
    public ResponseEntity<SimulationResultDto> runSimulation(@RequestBody SimulationRequestDto request) {
        SimulationResultDto result = simulationService.simulateScenario(request);
        return ResponseEntity.ok(result);
    }
}
