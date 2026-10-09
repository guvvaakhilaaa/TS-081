package com.ecoimpact.service;

import com.ecoimpact.dto.ActivityInputDto;
import com.ecoimpact.dto.CarbonResultDto;
import com.ecoimpact.dto.CategoryDetailDto;
import com.ecoimpact.model.*;
import com.ecoimpact.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class CarbonCalculationService {

    private final EmissionFactorService factorService;
    private final ActivityRecordRepository activityRecordRepository;
    private final CarbonAssessmentRepository assessmentRepository;
    private final CategoryEmissionRepository categoryEmissionRepository;
    private final ProgressHistoryRepository progressRepository;
    private final UserRepository userRepository;

    public CarbonCalculationService(EmissionFactorService factorService,
                                    ActivityRecordRepository activityRecordRepository,
                                    CarbonAssessmentRepository assessmentRepository,
                                    CategoryEmissionRepository categoryEmissionRepository,
                                    ProgressHistoryRepository progressRepository,
                                    UserRepository userRepository) {
        this.factorService = factorService;
        this.activityRecordRepository = activityRecordRepository;
        this.assessmentRepository = assessmentRepository;
        this.categoryEmissionRepository = categoryEmissionRepository;
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CarbonResultDto calculateAndSave(ActivityInputDto input) {
        User user = userRepository.findById(input.getUserId()).orElseGet(() -> {
            User newUser = new User("guest_user", "guest@ecoimpact.org", "pass", "Guest Eco User");
            return userRepository.save(newUser);
        });

        // 1. Save Activity Record
        ActivityRecord record = new ActivityRecord();
        record.setUser(user);
        record.setReportingPeriod(input.getReportingPeriod());
        record.setRecordDate(LocalDate.now());

        record.setVehicleType(input.getVehicleType());
        record.setDistanceKm(input.getDistanceKm() != null ? input.getDistanceKm() : BigDecimal.ZERO);
        record.setMileageKmPerLitre(input.getMileageKmPerLitre() != null && input.getMileageKmPerLitre().compareTo(BigDecimal.ZERO) > 0 ? input.getMileageKmPerLitre() : new BigDecimal("15.00"));
        
        // Compute fuel litres if distance > 0 and fuel consumed not directly entered
        BigDecimal mileage = record.getMileageKmPerLitre();
        BigDecimal fuelLitres = input.getFuelConsumedLitres();
        if ((fuelLitres == null || fuelLitres.compareTo(BigDecimal.ZERO) == 0) && record.getDistanceKm().compareTo(BigDecimal.ZERO) > 0) {
            fuelLitres = record.getDistanceKm().divide(mileage, 2, RoundingMode.HALF_UP);
        } else if (fuelLitres == null) {
            fuelLitres = BigDecimal.ZERO;
        }
        record.setFuelConsumedLitres(fuelLitres);
        record.setPublicTransportKm(input.getPublicTransportKm() != null ? input.getPublicTransportKm() : BigDecimal.ZERO);
        record.setFlightKm(input.getFlightKm() != null ? input.getFlightKm() : BigDecimal.ZERO);
        record.setEvEnergyKwh(input.getEvEnergyKwh() != null ? input.getEvEnergyKwh() : BigDecimal.ZERO);

        record.setElectricityKwh(input.getElectricityKwh() != null ? input.getElectricityKwh() : BigDecimal.ZERO);
        record.setGridRegion(input.getGridRegion() != null ? input.getGridRegion() : "India National Grid");
        record.setLpgCylindersOrKg(input.getLpgCylindersOrKg() != null ? input.getLpgCylindersOrKg() : BigDecimal.ZERO);
        record.setNaturalGasKwh(input.getNaturalGasKwh() != null ? input.getNaturalGasKwh() : BigDecimal.ZERO);

        record.setDietType(input.getDietType() != null ? input.getDietType() : "MIXED");
        record.setMeatServingsPerWeek(input.getMeatServingsPerWeek() != null ? input.getMeatServingsPerWeek() : 4);
        record.setDairyServingsPerWeek(input.getDairyServingsPerWeek() != null ? input.getDairyServingsPerWeek() : 7);
        record.setFoodWasteKg(input.getFoodWasteKg() != null ? input.getFoodWasteKg() : new BigDecimal("2.00"));

        record.setClothingItemsBought(input.getClothingItemsBought() != null ? input.getClothingItemsBought() : 0);
        record.setElectronicsBought(input.getElectronicsBought() != null ? input.getElectronicsBought() : 0);
        record.setGeneralGoodsSpendUsd(input.getGeneralGoodsSpendUsd() != null ? input.getGeneralGoodsSpendUsd() : BigDecimal.ZERO);

        record.setWasteGeneratedKg(input.getWasteGeneratedKg() != null ? input.getWasteGeneratedKg() : new BigDecimal("15.00"));
        record.setRecyclingPercentage(input.getRecyclingPercentage() != null ? input.getRecyclingPercentage() : new BigDecimal("20.00"));
        record.setCompostingActive(input.getCompostingActive() != null ? input.getCompostingActive() : false);

        record.setIsIndustrial(input.getIsIndustrial() != null ? input.getIsIndustrial() : false);
        record.setIndustrialEnergyKwh(input.getIndustrialEnergyKwh() != null ? input.getIndustrialEnergyKwh() : BigDecimal.ZERO);
        record.setIndustrialFuelLitres(input.getIndustrialFuelLitres() != null ? input.getIndustrialFuelLitres() : BigDecimal.ZERO);

        ActivityRecord savedRecord = activityRecordRepository.save(record);

        // 2. Perform Category-by-Category Scientific Carbon Calculation
        Map<String, CategoryDetailDto> calculationDetails = calculateBreakdown(record);

        BigDecimal totalFootprintKg = calculationDetails.values().stream()
                .map(CategoryDetailDto::getEmissionsKg)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // Safe percentage calculations
        String largestCategory = "TRANSPORTATION";
        BigDecimal largestEmissions = BigDecimal.ZERO;

        for (Map.Entry<String, CategoryDetailDto> entry : calculationDetails.entrySet()) {
            CategoryDetailDto detail = entry.getValue();
            if (totalFootprintKg.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal pct = detail.getEmissionsKg()
                        .multiply(new BigDecimal("100"))
                        .divide(totalFootprintKg, 2, RoundingMode.HALF_UP);
                detail.setPercentageContribution(pct);
            } else {
                detail.setPercentageContribution(BigDecimal.ZERO);
            }
            if (detail.getEmissionsKg().compareTo(largestEmissions) > 0) {
                largestEmissions = detail.getEmissionsKg();
                largestCategory = entry.getKey();
            }
        }

        BigDecimal totalFootprintTonnes = totalFootprintKg.divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP);
        BigDecimal annualProjectionTonnes = totalFootprintTonnes.multiply(new BigDecimal("12")).setScale(4, RoundingMode.HALF_UP);

        // 3. Save Carbon Assessment
        CarbonAssessment assessment = new CarbonAssessment();
        assessment.setUser(user);
        assessment.setActivityRecordId(savedRecord.getId());
        assessment.setAssessmentDate(LocalDate.now());
        assessment.setTotalFootprintKg(totalFootprintKg);
        assessment.setTotalFootprintTonnes(totalFootprintTonnes);
        assessment.setLargestCategory(largestCategory);
        assessment.setAnnualProjectionTonnes(annualProjectionTonnes);
        assessment.setReportingPeriod(input.getReportingPeriod());

        CarbonAssessment savedAssessment = assessmentRepository.save(assessment);

        // 4. Save Category Emissions
        for (CategoryDetailDto catDetail : calculationDetails.values()) {
            CategoryEmission catEmission = new CategoryEmission(
                    savedAssessment,
                    catDetail.getCategory(),
                    catDetail.getEmissionsKg(),
                    catDetail.getPercentageContribution()
            );
            categoryEmissionRepository.save(catEmission);
        }

        // 5. Update Progress History
        ProgressHistory history = new ProgressHistory();
        history.setUser(user);
        history.setAssessmentId(savedAssessment.getId());
        history.setPeriodLabel(LocalDate.now().getMonth().name().substring(0, 3) + " " + LocalDate.now().getYear());
        history.setTotalFootprintKg(totalFootprintKg);
        history.setTransportKg(calculationDetails.getOrDefault("TRANSPORTATION", new CategoryDetailDto()).getEmissionsKg());
        history.setEnergyKg(calculationDetails.getOrDefault("RESIDENTIAL", new CategoryDetailDto()).getEmissionsKg());
        history.setFoodKg(calculationDetails.getOrDefault("FOOD", new CategoryDetailDto()).getEmissionsKg());
        history.setWasteKg(calculationDetails.getOrDefault("WASTE", new CategoryDetailDto()).getEmissionsKg());
        history.setShoppingKg(calculationDetails.getOrDefault("SHOPPING", new CategoryDetailDto()).getEmissionsKg());
        progressRepository.save(history);

        // 6. Build Result DTO
        CarbonResultDto result = new CarbonResultDto();
        result.setAssessmentId(savedAssessment.getId());
        result.setUserId(user.getId());
        result.setAssessmentDate(savedAssessment.getAssessmentDate());
        result.setTotalFootprintKg(totalFootprintKg);
        result.setTotalFootprintTonnes(totalFootprintTonnes);
        result.setLargestCategory(largestCategory);
        result.setAnnualProjectionTonnes(annualProjectionTonnes);
        result.setReportingPeriod(savedAssessment.getReportingPeriod());
        result.setCategories(new ArrayList<>(calculationDetails.values()));

        return result;
    }

    public Map<String, CategoryDetailDto> calculateBreakdown(ActivityRecord r) {
        Map<String, CategoryDetailDto> map = new LinkedHashMap<>();

        // A. TRANSPORTATION
        // Vehicle emissions: fuel litres * factor
        String fuelType = r.getFuelType() != null ? r.getFuelType() : "PETROL";
        BigDecimal fuelFactor = fuelType.equalsIgnoreCase("DIESEL") ?
                factorService.getFactorValue("TRANSPORTATION", "DIESEL_CAR", new BigDecimal("2.68")) :
                factorService.getFactorValue("TRANSPORTATION", "PETROL_CAR", new BigDecimal("2.31"));

        BigDecimal vehicleEmissions = r.getFuelConsumedLitres().multiply(fuelFactor);

        // Public transit
        BigDecimal busFactor = factorService.getFactorValue("TRANSPORTATION", "CITY_BUS", new BigDecimal("0.089"));
        BigDecimal publicTransitEmissions = r.getPublicTransportKm().multiply(busFactor);

        // Flight emissions
        BigDecimal flightFactor = factorService.getFactorValue("TRANSPORTATION", "DOMESTIC_FLIGHT", new BigDecimal("0.254"));
        BigDecimal flightEmissions = r.getFlightKm().multiply(flightFactor);

        // EV energy emissions (using grid factor)
        BigDecimal gridFactor = factorService.getFactorValue("RESIDENTIAL", "ELECTRICITY_INDIA", new BigDecimal("0.82"));
        BigDecimal evEmissions = r.getEvEnergyKwh().multiply(gridFactor);

        BigDecimal totalTransport = vehicleEmissions.add(publicTransitEmissions).add(flightEmissions).add(evEmissions).setScale(2, RoundingMode.HALF_UP);
        map.put("TRANSPORTATION", new CategoryDetailDto("TRANSPORTATION", totalTransport, BigDecimal.ZERO,
                "Fuel (L) × Fuel Factor + Public Transit (km) × 0.089 + Flight (km) × 0.254 + EV (kWh) × Grid Factor",
                "Fuel Factor (" + fuelFactor + " kg CO2e/L), Bus Factor (" + busFactor + " kg/p-km)"));

        // B. RESIDENTIAL & ENERGY
        BigDecimal elecEmissions = r.getElectricityKwh().multiply(gridFactor);
        BigDecimal lpgFactor = factorService.getFactorValue("RESIDENTIAL", "LPG_COOKING", new BigDecimal("2.984"));
        BigDecimal lpgEmissions = r.getLpgCylindersOrKg().multiply(lpgFactor);
        BigDecimal natGasFactor = factorService.getFactorValue("RESIDENTIAL", "NATURAL_GAS_PNG", new BigDecimal("0.202"));
        BigDecimal natGasEmissions = r.getNaturalGasKwh().multiply(natGasFactor);

        BigDecimal totalEnergy = elecEmissions.add(lpgEmissions).add(natGasEmissions).setScale(2, RoundingMode.HALF_UP);
        map.put("RESIDENTIAL", new CategoryDetailDto("RESIDENTIAL", totalEnergy, BigDecimal.ZERO,
                "Electricity (kWh) × Grid Factor + LPG (kg) × 2.984 + Natural Gas (kWh) × 0.202",
                "Grid Factor (" + gridFactor + " kg/kWh), LPG (" + lpgFactor + " kg/kg)"));

        // C. AGRICULTURE & FOOD
        // 30 days monthly baseline
        BigDecimal dietDailyFactor;
        String diet = r.getDietType() != null ? r.getDietType().toUpperCase() : "MIXED";
        if (diet.contains("VEGAN")) {
            dietDailyFactor = factorService.getFactorValue("FOOD", "VEGAN_DIET", new BigDecimal("2.90"));
        } else if (diet.contains("VEGETARIAN")) {
            dietDailyFactor = factorService.getFactorValue("FOOD", "VEGETARIAN_DIET", new BigDecimal("3.80"));
        } else if (diet.contains("MEAT_HEAVY")) {
            dietDailyFactor = factorService.getFactorValue("FOOD", "MEAT_HEAVY_DIET", new BigDecimal("7.20"));
        } else {
            dietDailyFactor = factorService.getFactorValue("FOOD", "MEDIUM_MEAT_DIET", new BigDecimal("5.60"));
        }

        BigDecimal dietMonthlyEmissions = dietDailyFactor.multiply(new BigDecimal("30"));
        BigDecimal foodWasteFactor = factorService.getFactorValue("FOOD", "FOOD_WASTE", new BigDecimal("2.50"));
        BigDecimal foodWasteEmissions = r.getFoodWasteKg().multiply(foodWasteFactor);

        BigDecimal totalFood = dietMonthlyEmissions.add(foodWasteEmissions).setScale(2, RoundingMode.HALF_UP);
        map.put("FOOD", new CategoryDetailDto("FOOD", totalFood, BigDecimal.ZERO,
                "Dietary Pattern (30 days × " + dietDailyFactor + " kg/day) + Food Waste (kg) × 2.50 kg CO2e/kg",
                "Diet Baseline (" + diet + ": " + dietDailyFactor + " kg/day), Food Waste Factor (2.50 kg/kg)"));

        // D. SHOPPING & GOODS
        BigDecimal clothingFactor = factorService.getFactorValue("SHOPPING", "CLOTHING_FAST_FASHION", new BigDecimal("15.00"));
        BigDecimal clothingEmissions = new BigDecimal(r.getClothingItemsBought()).multiply(clothingFactor);

        BigDecimal techFactor = factorService.getFactorValue("SHOPPING", "ELECTRONICS_SMARTPHONE", new BigDecimal("60.00"));
        BigDecimal techEmissions = new BigDecimal(r.getElectronicsBought()).multiply(techFactor);

        BigDecimal spendFactor = factorService.getFactorValue("SHOPPING", "GENERAL_GOODS_SPEND", new BigDecimal("0.35"));
        BigDecimal spendEmissions = r.getGeneralGoodsSpendUsd().multiply(spendFactor);

        BigDecimal totalShopping = clothingEmissions.add(techEmissions).add(spendEmissions).setScale(2, RoundingMode.HALF_UP);
        map.put("SHOPPING", new CategoryDetailDto("SHOPPING", totalShopping, BigDecimal.ZERO,
                "Clothing Items × 15 kg + Electronics × 60 kg + Spend ($) × 0.35 kg/USD proxy",
                "Embodied garment carbon (15 kg), Device life-cycle (60 kg), Spend proxy (0.35 kg/$)"));

        // E. WASTE MANAGEMENT
        BigDecimal totalWaste = r.getWasteGeneratedKg();
        BigDecimal recyclePct = r.getRecyclingPercentage() != null ? r.getRecyclingPercentage() : BigDecimal.ZERO;
        BigDecimal recycledKg = totalWaste.multiply(recyclePct).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal landfillKg = totalWaste.subtract(recycledKg).max(BigDecimal.ZERO);

        BigDecimal landfillFactor = factorService.getFactorValue("WASTE", "MIXED_MUNICIPAL_WASTE", new BigDecimal("0.58"));
        BigDecimal recycleCreditFactor = factorService.getFactorValue("WASTE", "RECYCLED_MATERIALS", new BigDecimal("-0.45"));

        BigDecimal landfillEmissions = landfillKg.multiply(landfillFactor);
        BigDecimal recycleCredit = recycledKg.multiply(recycleCreditFactor); // negative credit

        BigDecimal compostingDiscount = (r.getCompostingActive() != null && r.getCompostingActive()) ? new BigDecimal("3.50") : BigDecimal.ZERO;

        BigDecimal totalWasteEmissions = landfillEmissions.add(recycleCredit).subtract(compostingDiscount).max(new BigDecimal("0.50")).setScale(2, RoundingMode.HALF_UP);
        map.put("WASTE", new CategoryDetailDto("WASTE", totalWasteEmissions, BigDecimal.ZERO,
                "Landfill Waste (kg) × 0.58 - Recycled (kg) × 0.45 - Composting Credit (3.5 kg)",
                "Landfill methane factor (0.58 kg/kg), Recycling displacement credit (-0.45 kg/kg)"));

        // F. INDUSTRIAL (if enabled)
        if (r.getIsIndustrial() != null && r.getIsIndustrial()) {
            BigDecimal indEnergyEmissions = r.getIndustrialEnergyKwh().multiply(gridFactor);
            BigDecimal indFuelEmissions = r.getIndustrialFuelLitres().multiply(new BigDecimal("2.68"));
            BigDecimal totalIndustrial = indEnergyEmissions.add(indFuelEmissions).setScale(2, RoundingMode.HALF_UP);
            map.put("INDUSTRIAL", new CategoryDetailDto("INDUSTRIAL", totalIndustrial, BigDecimal.ZERO,
                    "Industrial Energy (kWh) × Grid Factor + Industrial Fuel (L) × 2.68",
                    "Organizational Scope 1 & 2 boundary factors"));
        }

        return map;
    }
}
