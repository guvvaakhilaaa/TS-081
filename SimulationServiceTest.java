package com.ecoimpact;

import com.ecoimpact.dto.CategoryDetailDto;
import com.ecoimpact.dto.SimulationRequestDto;
import com.ecoimpact.dto.SimulationResultDto;
import com.ecoimpact.model.ActivityRecord;
import com.ecoimpact.model.User;
import com.ecoimpact.repository.ActivityRecordRepository;
import com.ecoimpact.repository.SimulationScenarioRepository;
import com.ecoimpact.repository.UserRepository;
import com.ecoimpact.service.CarbonCalculationService;
import com.ecoimpact.service.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SimulationServiceTest {

    private ActivityRecordRepository activityRecordRepository;
    private CarbonCalculationService calculationService;
    private SimulationScenarioRepository simulationScenarioRepository;
    private UserRepository userRepository;
    private SimulationService simulationService;

    @BeforeEach
    void setUp() {
        activityRecordRepository = Mockito.mock(ActivityRecordRepository.class);
        calculationService = Mockito.mock(CarbonCalculationService.class);
        simulationScenarioRepository = Mockito.mock(SimulationScenarioRepository.class);
        userRepository = Mockito.mock(UserRepository.class);

        simulationService = new SimulationService(
                activityRecordRepository, calculationService, simulationScenarioRepository, userRepository
        );
    }

    @Test
    void testSimulationReductionCalculations() {
        ActivityRecord baselineRecord = new ActivityRecord();
        baselineRecord.setDistanceKm(new BigDecimal("300.00"));
        baselineRecord.setFuelConsumedLitres(new BigDecimal("20.00"));
        baselineRecord.setElectricityKwh(new BigDecimal("200.00"));

        when(activityRecordRepository.findFirstByUserIdOrderByRecordDateDesc(1L))
                .thenReturn(Optional.of(baselineRecord));

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Baseline calculation: 500 kg
        Map<String, CategoryDetailDto> baseMap = new LinkedHashMap<>();
        baseMap.put("TRANSPORTATION", new CategoryDetailDto("TRANSPORTATION", new BigDecimal("200.00"), new BigDecimal("40.00"), "", ""));
        baseMap.put("RESIDENTIAL", new CategoryDetailDto("RESIDENTIAL", new BigDecimal("300.00"), new BigDecimal("60.00"), "", ""));
        
        // Proposed calculation: 400 kg (100 kg reduction)
        Map<String, CategoryDetailDto> propMap = new LinkedHashMap<>();
        propMap.put("TRANSPORTATION", new CategoryDetailDto("TRANSPORTATION", new BigDecimal("140.00"), new BigDecimal("35.00"), "", ""));
        propMap.put("RESIDENTIAL", new CategoryDetailDto("RESIDENTIAL", new BigDecimal("260.00"), new BigDecimal("65.00"), "", ""));

        when(calculationService.calculateBreakdown(any(ActivityRecord.class)))
                .thenReturn(baseMap)
                .thenReturn(propMap);

        SimulationRequestDto req = new SimulationRequestDto();
        req.setUserId(1L);
        req.setScenarioName("Test Modal Shift");
        req.setTransportReductionPercent(new BigDecimal("30.00"));
        req.setElectricityReductionPercent(new BigDecimal("20.00"));

        SimulationResultDto result = simulationService.simulateScenario(req);

        assertNotNull(result);
        assertEquals(new BigDecimal("500.00"), result.getBaselineEmissionsKg());
        assertEquals(new BigDecimal("400.00"), result.getProposedEmissionsKg());
        assertEquals(new BigDecimal("100.00"), result.getReductionKg());
        // (100 / 500) * 100 = 20.00%
        assertEquals(new BigDecimal("20.00"), result.getReductionPercentage());
    }
}
