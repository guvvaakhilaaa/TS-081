package com.ecoimpact;

import com.ecoimpact.dto.ActivityInputDto;
import com.ecoimpact.dto.CategoryDetailDto;
import com.ecoimpact.model.ActivityRecord;
import com.ecoimpact.model.EmissionFactor;
import com.ecoimpact.repository.ActivityRecordRepository;
import com.ecoimpact.repository.CarbonAssessmentRepository;
import com.ecoimpact.repository.CategoryEmissionRepository;
import com.ecoimpact.repository.ProgressHistoryRepository;
import com.ecoimpact.repository.UserRepository;
import com.ecoimpact.service.CarbonCalculationService;
import com.ecoimpact.service.EmissionFactorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CarbonCalculationServiceTest {

    private EmissionFactorService factorService;
    private CarbonCalculationService calculationService;

    @BeforeEach
    void setUp() {
        factorService = Mockito.mock(EmissionFactorService.class);
        ActivityRecordRepository activityRecordRepository = Mockito.mock(ActivityRecordRepository.class);
        CarbonAssessmentRepository assessmentRepository = Mockito.mock(CarbonAssessmentRepository.class);
        CategoryEmissionRepository categoryEmissionRepository = Mockito.mock(CategoryEmissionRepository.class);
        ProgressHistoryRepository progressRepository = Mockito.mock(ProgressHistoryRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        // Mock common factors
        when(factorService.getFactorValue(eq("TRANSPORTATION"), eq("PETROL_CAR"), any())).thenReturn(new BigDecimal("2.31"));
        when(factorService.getFactorValue(eq("TRANSPORTATION"), eq("CITY_BUS"), any())).thenReturn(new BigDecimal("0.089"));
        when(factorService.getFactorValue(eq("TRANSPORTATION"), eq("DOMESTIC_FLIGHT"), any())).thenReturn(new BigDecimal("0.254"));
        when(factorService.getFactorValue(eq("RESIDENTIAL"), eq("ELECTRICITY_INDIA"), any())).thenReturn(new BigDecimal("0.82"));
        when(factorService.getFactorValue(eq("RESIDENTIAL"), eq("LPG_COOKING"), any())).thenReturn(new BigDecimal("2.984"));
        when(factorService.getFactorValue(eq("RESIDENTIAL"), eq("NATURAL_GAS_PNG"), any())).thenReturn(new BigDecimal("0.202"));
        when(factorService.getFactorValue(eq("FOOD"), eq("MEDIUM_MEAT_DIET"), any())).thenReturn(new BigDecimal("5.60"));
        when(factorService.getFactorValue(eq("FOOD"), eq("FOOD_WASTE"), any())).thenReturn(new BigDecimal("2.50"));
        when(factorService.getFactorValue(eq("SHOPPING"), eq("CLOTHING_FAST_FASHION"), any())).thenReturn(new BigDecimal("15.00"));
        when(factorService.getFactorValue(eq("SHOPPING"), eq("ELECTRONICS_SMARTPHONE"), any())).thenReturn(new BigDecimal("60.00"));
        when(factorService.getFactorValue(eq("SHOPPING"), eq("GENERAL_GOODS_SPEND"), any())).thenReturn(new BigDecimal("0.35"));
        when(factorService.getFactorValue(eq("WASTE"), eq("MIXED_MUNICIPAL_WASTE"), any())).thenReturn(new BigDecimal("0.58"));
        when(factorService.getFactorValue(eq("WASTE"), eq("RECYCLED_MATERIALS"), any())).thenReturn(new BigDecimal("-0.45"));

        calculationService = new CarbonCalculationService(
                factorService, activityRecordRepository, assessmentRepository,
                categoryEmissionRepository, progressRepository, userRepository
        );
    }

    @Test
    void testTransportationCalculationFormula() {
        ActivityRecord record = new ActivityRecord();
        record.setFuelType("PETROL");
        record.setFuelConsumedLitres(new BigDecimal("25.00")); // 25 L * 2.31 = 57.75 kg
        record.setPublicTransportKm(new BigDecimal("50.00")); // 50 * 0.089 = 4.45 kg
        record.setFlightKm(BigDecimal.ZERO);
        record.setEvEnergyKwh(BigDecimal.ZERO);
        record.setElectricityKwh(BigDecimal.ZERO);
        record.setLpgCylindersOrKg(BigDecimal.ZERO);
        record.setNaturalGasKwh(BigDecimal.ZERO);
        record.setDietType("MEDIUM_MEAT_DIET");
        record.setClothingItemsBought(0);
        record.setElectronicsBought(0);
        record.setGeneralGoodsSpendUsd(BigDecimal.ZERO);
        record.setWasteGeneratedKg(BigDecimal.ZERO);
        record.setRecyclingPercentage(BigDecimal.ZERO);

        Map<String, CategoryDetailDto> breakdown = calculationService.calculateBreakdown(record);
        assertNotNull(breakdown);
        CategoryDetailDto transport = breakdown.get("TRANSPORTATION");
        assertNotNull(transport);
        // Expected = 57.75 + 4.45 = 62.20 kg
        assertEquals(new BigDecimal("62.20"), transport.getEmissionsKg());
    }

    @Test
    void testResidentialElectricityAndLpgCalculation() {
        ActivityRecord record = new ActivityRecord();
        record.setFuelConsumedLitres(BigDecimal.ZERO);
        record.setPublicTransportKm(BigDecimal.ZERO);
        record.setFlightKm(BigDecimal.ZERO);
        record.setEvEnergyKwh(BigDecimal.ZERO);
        record.setElectricityKwh(new BigDecimal("100.00")); // 100 * 0.82 = 82.00 kg
        record.setLpgCylindersOrKg(new BigDecimal("10.00")); // 10 * 2.984 = 29.84 kg
        record.setNaturalGasKwh(BigDecimal.ZERO);
        record.setDietType("MEDIUM_MEAT_DIET");
        record.setClothingItemsBought(0);
        record.setElectronicsBought(0);
        record.setGeneralGoodsSpendUsd(BigDecimal.ZERO);
        record.setWasteGeneratedKg(BigDecimal.ZERO);
        record.setRecyclingPercentage(BigDecimal.ZERO);

        Map<String, CategoryDetailDto> breakdown = calculationService.calculateBreakdown(record);
        CategoryDetailDto residential = breakdown.get("RESIDENTIAL");
        assertNotNull(residential);
        // 82.00 + 29.84 = 111.84 kg
        assertEquals(new BigDecimal("111.84"), residential.getEmissionsKg());
    }
}
