package com.ecoimpact.service;

import com.ecoimpact.dto.HotspotResultDto;
import com.ecoimpact.model.CarbonAssessment;
import com.ecoimpact.model.CategoryEmission;
import com.ecoimpact.repository.CarbonAssessmentRepository;
import com.ecoimpact.repository.CategoryEmissionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class HotspotAnalysisService {

    private final CarbonAssessmentRepository assessmentRepository;
    private final CategoryEmissionRepository categoryEmissionRepository;

    public HotspotAnalysisService(CarbonAssessmentRepository assessmentRepository,
                                  CategoryEmissionRepository categoryEmissionRepository) {
        this.assessmentRepository = assessmentRepository;
        this.categoryEmissionRepository = categoryEmissionRepository;
    }

    /**
     * Algorithm 4: Emission Hotspot Detection
     * Dynamically ranks categories based on actual calculated values.
     */
    public HotspotResultDto detectHotspots(Long userId) {
        Optional<CarbonAssessment> latestOpt = assessmentRepository.findFirstByUserIdOrderByAssessmentDateDesc(userId);
        if (latestOpt.isEmpty()) {
            return getEmptyHotspotResult();
        }

        CarbonAssessment assessment = latestOpt.get();
        List<CategoryEmission> categoryList = categoryEmissionRepository.findByCarbonAssessmentId(assessment.getId());

        // Sort descending by emissions_kg
        List<CategoryEmission> sortedList = new ArrayList<>(categoryList);
        sortedList.sort(Comparator.comparing(CategoryEmission::getEmissionsKg).reversed());

        HotspotResultDto dto = new HotspotResultDto();
        List<HotspotResultDto.RankedCategoryDto> rankedCategories = new ArrayList<>();

        int rank = 1;
        for (CategoryEmission ce : sortedList) {
            rankedCategories.add(new HotspotResultDto.RankedCategoryDto(
                    ce.getCategory(),
                    ce.getEmissionsKg(),
                    ce.getPercentageContribution(),
                    rank++
            ));
        }
        dto.setRankedCategories(rankedCategories);

        if (!sortedList.isEmpty()) {
            CategoryEmission primary = sortedList.get(0);
            dto.setPrimaryHotspot(primary.getCategory());
            dto.setPrimaryHotspotPercentage(primary.getPercentageContribution());
            dto.setPrimaryHotspotKg(primary.getEmissionsKg());
        }

        if (sortedList.size() > 1) {
            CategoryEmission secondary = sortedList.get(1);
            dto.setSecondaryHotspot(secondary.getCategory());
            dto.setSecondaryHotspotPercentage(secondary.getPercentageContribution());
        }

        // Identify activity hotspots & specific reduction opportunities
        List<String> activityHotspots = new ArrayList<>();
        List<String> reductionOpportunities = new ArrayList<>();

        for (HotspotResultDto.RankedCategoryDto rc : rankedCategories) {
            if (rc.getRank() <= 2) {
                switch (rc.getCategory()) {
                    case "RESIDENTIAL":
                        activityHotspots.add("Grid electricity & domestic heating contribute " + rc.getPercentage() + "% of footprint.");
                        reductionOpportunities.add("Solar rooftop or high-efficiency star-rated inverter appliances.");
                        break;
                    case "FOOD":
                        activityHotspots.add("Dietary protein lifecycle & household food waste make up " + rc.getPercentage() + "%.");
                        reductionOpportunities.add("Transition to plant-rich meals and zero-waste grocery planning.");
                        break;
                    case "TRANSPORTATION":
                        activityHotspots.add("Single-occupancy internal combustion vehicle travel is a major source (" + rc.getPercentage() + "%).");
                        reductionOpportunities.add("Modal shift to metro/electric transit or hybrid commuting 3 days/week.");
                        break;
                    case "SHOPPING":
                        activityHotspots.add("Frequent consumer goods & apparel purchases drive " + rc.getPercentage() + "% of footprint.");
                        reductionOpportunities.add("Mindful purchasing, second-hand reuse, and durable quality items.");
                        break;
                    case "WASTE":
                        activityHotspots.add("Landfill organic waste decomposition generates methane (" + rc.getPercentage() + "%).");
                        reductionOpportunities.add("Source segregation, kitchen composting, and recycling 80%+ of dry paper/plastics.");
                        break;
                }
            }
        }

        dto.setActivityHotspots(activityHotspots);
        dto.setReductionOpportunities(reductionOpportunities);

        return dto;
    }

    private HotspotResultDto getEmptyHotspotResult() {
        HotspotResultDto dto = new HotspotResultDto();
        dto.setPrimaryHotspot("NONE");
        dto.setPrimaryHotspotPercentage(BigDecimal.ZERO);
        dto.setPrimaryHotspotKg(BigDecimal.ZERO);
        dto.setSecondaryHotspot("NONE");
        dto.setSecondaryHotspotPercentage(BigDecimal.ZERO);
        return dto;
    }
}
