package com.ecoimpact.service;

import com.ecoimpact.dto.CategoryDetailDto;
import com.ecoimpact.dto.SimulationRequestDto;
import com.ecoimpact.dto.SimulationResultDto;
import com.ecoimpact.model.*;
import com.ecoimpact.repository.ActivityRecordRepository;
import com.ecoimpact.repository.SimulationScenarioRepository;
import com.ecoimpact.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class SimulationService {

    private final ActivityRecordRepository activityRecordRepository;
    private final CarbonCalculationService calculationService;
    private final SimulationScenarioRepository simulationScenarioRepository;
    private final UserRepository userRepository;

    public SimulationService(ActivityRecordRepository activityRecordRepository,
                             CarbonCalculationService calculationService,
                             SimulationScenarioRepository simulationScenarioRepository,
                             UserRepository userRepository) {
        this.activityRecordRepository = activityRecordRepository;
        this.calculationService = calculationService;
        this.simulationScenarioRepository = simulationScenarioRepository;
        this.userRepository = userRepository;
    }

    /**
     * Algorithm 3: What-If Simulation Algorithm
     */
    @Transactional
    public SimulationResultDto simulateScenario(SimulationRequestDto req) {
        // 1. Load user baseline activity record
        ActivityRecord baseline = activityRecordRepository.findFirstByUserIdOrderByRecordDateDesc(req.getUserId())
                .orElseGet(() -> {
                    // Create realistic baseline if none exists
                    ActivityRecord defaultRec = new ActivityRecord();
                    defaultRec.setDistanceKm(new BigDecimal("400.00"));
                    defaultRec.setFuelConsumedLitres(new BigDecimal("26.67"));
                    defaultRec.setElectricityKwh(new BigDecimal("220.00"));
                    defaultRec.setLpgCylindersOrKg(new BigDecimal("14.20"));
                    defaultRec.setDietType("MIXED");
                    defaultRec.setWasteGeneratedKg(new BigDecimal("20.00"));
                    return defaultRec;
                });

        // 2. Calculate baseline breakdown
        Map<String, CategoryDetailDto> baselineBreakdown = calculationService.calculateBreakdown(baseline);
        BigDecimal baselineTotal = baselineBreakdown.values().stream()
                .map(CategoryDetailDto::getEmissionsKg)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 3. Clone and apply proposed modifications to create proposed activity record
        ActivityRecord proposed = cloneActivityRecord(baseline);

        // A. Apply Transportation Change
        if (req.getTransportReductionPercent() != null && req.getTransportReductionPercent().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal factor = BigDecimal.ONE.subtract(req.getTransportReductionPercent().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
            proposed.setFuelConsumedLitres(baseline.getFuelConsumedLitres().multiply(factor).setScale(2, RoundingMode.HALF_UP));
            proposed.setDistanceKm(baseline.getDistanceKm().multiply(factor).setScale(2, RoundingMode.HALF_UP));
            
            // If shifting to public transit, add proportional public transport km
            if (req.getPublicTransitDaysPerWeek() != null && req.getPublicTransitDaysPerWeek() > 0) {
                BigDecimal shiftedKm = baseline.getDistanceKm().multiply(req.getTransportReductionPercent().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                proposed.setPublicTransportKm(baseline.getPublicTransportKm().add(shiftedKm));
            }
        }

        // B. Apply Residential / Electricity Change
        if (req.getElectricityReductionPercent() != null && req.getElectricityReductionPercent().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal factor = BigDecimal.ONE.subtract(req.getElectricityReductionPercent().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
            proposed.setElectricityKwh(baseline.getElectricityKwh().multiply(factor).setScale(2, RoundingMode.HALF_UP));
        }

        // C. Apply Food & Diet Change
        if (req.getProposedDietType() != null && !req.getProposedDietType().isEmpty()) {
            proposed.setDietType(req.getProposedDietType());
        }
        if (req.getFoodWasteReductionPercent() != null && req.getFoodWasteReductionPercent().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal factor = BigDecimal.ONE.subtract(req.getFoodWasteReductionPercent().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
            proposed.setFoodWasteKg(baseline.getFoodWasteKg().multiply(factor).setScale(2, RoundingMode.HALF_UP));
        }

        // D. Apply Waste Management Change
        if (req.getProposedRecyclingPercent() != null) {
            proposed.setRecyclingPercentage(req.getProposedRecyclingPercent());
        }
        if (req.getEnableComposting() != null) {
            proposed.setCompostingActive(req.getEnableComposting());
        }

        // E. Apply Shopping Change
        if (req.getShoppingReductionPercent() != null && req.getShoppingReductionPercent().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal factor = BigDecimal.ONE.subtract(req.getShoppingReductionPercent().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
            proposed.setGeneralGoodsSpendUsd(baseline.getGeneralGoodsSpendUsd().multiply(factor).setScale(2, RoundingMode.HALF_UP));
        }

        // 4. Calculate proposed breakdown
        Map<String, CategoryDetailDto> proposedBreakdown = calculationService.calculateBreakdown(proposed);
        BigDecimal proposedTotal = proposedBreakdown.values().stream()
                .map(CategoryDetailDto::getEmissionsKg)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 5. Calculate Difference & Percentage Change
        BigDecimal reductionKg = baselineTotal.subtract(proposedTotal).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal percentageReduction = BigDecimal.ZERO;
        if (baselineTotal.compareTo(BigDecimal.ZERO) > 0) {
            percentageReduction = reductionKg.multiply(new BigDecimal("100"))
                    .divide(baselineTotal, 2, RoundingMode.HALF_UP);
        }

        BigDecimal annualAvoidedTonnes = reductionKg.multiply(new BigDecimal("12"))
                .divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP);

        // Estimated financial savings calculation (fuel saved * $1.20/L + electricity saved * $0.12/kWh)
        BigDecimal fuelSaved = baseline.getFuelConsumedLitres().subtract(proposed.getFuelConsumedLitres()).max(BigDecimal.ZERO);
        BigDecimal kwhSaved = baseline.getElectricityKwh().subtract(proposed.getElectricityKwh()).max(BigDecimal.ZERO);
        BigDecimal savingsUsd = fuelSaved.multiply(new BigDecimal("1.20"))
                .add(kwhSaved.multiply(new BigDecimal("0.12")))
                .setScale(2, RoundingMode.HALF_UP);

        // 6. Build category comparisons
        List<SimulationResultDto.CategoryComparisonDto> categoryComparisons = new ArrayList<>();
        for (String cat : baselineBreakdown.keySet()) {
            BigDecimal baseVal = baselineBreakdown.get(cat).getEmissionsKg();
            BigDecimal propVal = proposedBreakdown.containsKey(cat) ? proposedBreakdown.get(cat).getEmissionsKg() : BigDecimal.ZERO;
            BigDecimal catDiff = baseVal.subtract(propVal).setScale(2, RoundingMode.HALF_UP);
            BigDecimal catPct = baseVal.compareTo(BigDecimal.ZERO) > 0 ?
                    catDiff.multiply(new BigDecimal("100")).divide(baseVal, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

            categoryComparisons.add(new SimulationResultDto.CategoryComparisonDto(cat, baseVal, propVal, catDiff, catPct));
        }

        // 7. Save Simulation Scenario to DB
        User user = userRepository.findById(req.getUserId()).orElse(null);
        if (user != null) {
            SimulationScenario scenario = new SimulationScenario();
            scenario.setUser(user);
            scenario.setScenarioName(req.getScenarioName() != null ? req.getScenarioName() : "Custom Simulation");
            scenario.setBaselineEmissionsKg(baselineTotal);
            scenario.setProposedEmissionsKg(proposedTotal);
            scenario.setReductionKg(reductionKg);
            scenario.setReductionPercentage(percentageReduction);
            scenario.setChangesSummary("Simulated modal shift, " + req.getElectricityReductionPercent() + "% energy saving, " + req.getProposedDietType() + " diet.");
            simulationScenarioRepository.save(scenario);
        }

        // 8. Return SimulationResultDto
        SimulationResultDto result = new SimulationResultDto();
        result.setScenarioName(req.getScenarioName());
        result.setBaselineEmissionsKg(baselineTotal);
        result.setProposedEmissionsKg(proposedTotal);
        result.setReductionKg(reductionKg);
        result.setReductionPercentage(percentageReduction);
        result.setAnnualAvoidedTonnes(annualAvoidedTonnes);
        result.setEstimatedMonthlySavingsUsd(savingsUsd);
        result.setCategoryComparisons(categoryComparisons);
        result.setSimulationSummary("By adopting these sustainable lifestyle choices, you could eliminate "
                + reductionKg + " kg CO2e (" + percentageReduction + "%) every month, saving an estimated $"
                + savingsUsd + " and avoiding " + annualAvoidedTonnes + " tonnes CO2e annually.");

        return result;
    }

    private ActivityRecord cloneActivityRecord(ActivityRecord src) {
        ActivityRecord target = new ActivityRecord();
        target.setVehicleType(src.getVehicleType());
        target.setDistanceKm(src.getDistanceKm());
        target.setFuelType(src.getFuelType());
        target.setMileageKmPerLitre(src.getMileageKmPerLitre());
        target.setFuelConsumedLitres(src.getFuelConsumedLitres());
        target.setPublicTransportKm(src.getPublicTransportKm());
        target.setFlightKm(src.getFlightKm());
        target.setEvEnergyKwh(src.getEvEnergyKwh());
        target.setElectricityKwh(src.getElectricityKwh());
        target.setGridRegion(src.getGridRegion());
        target.setLpgCylindersOrKg(src.getLpgCylindersOrKg());
        target.setNaturalGasKwh(src.getNaturalGasKwh());
        target.setDietType(src.getDietType());
        target.setMeatServingsPerWeek(src.getMeatServingsPerWeek());
        target.setDairyServingsPerWeek(src.getDairyServingsPerWeek());
        target.setFoodWasteKg(src.getFoodWasteKg());
        target.setClothingItemsBought(src.getClothingItemsBought());
        target.setElectronicsBought(src.getElectronicsBought());
        target.setGeneralGoodsSpendUsd(src.getGeneralGoodsSpendUsd());
        target.setWasteGeneratedKg(src.getWasteGeneratedKg());
        target.setRecyclingPercentage(src.getRecyclingPercentage());
        target.setCompostingActive(src.getCompostingActive());
        target.setIsIndustrial(src.getIsIndustrial());
        target.setIndustrialEnergyKwh(src.getIndustrialEnergyKwh());
        target.setIndustrialFuelLitres(src.getIndustrialFuelLitres());
        return target;
    }
}
