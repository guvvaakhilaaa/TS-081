package com.ecoimpact.service;

import com.ecoimpact.model.CarbonAssessment;
import com.ecoimpact.model.ProgressHistory;
import com.ecoimpact.repository.CarbonAssessmentRepository;
import com.ecoimpact.repository.ProgressHistoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ProgressTrackingService {

    private final ProgressHistoryRepository progressRepository;
    private final CarbonAssessmentRepository assessmentRepository;

    public ProgressTrackingService(ProgressHistoryRepository progressRepository,
                                   CarbonAssessmentRepository assessmentRepository) {
        this.progressRepository = progressRepository;
        this.assessmentRepository = assessmentRepository;
    }

    /**
     * Algorithm 6: Progress and Trend Analysis
     */
    public Map<String, Object> getProgressAnalytics(Long userId) {
        List<ProgressHistory> historyList = progressRepository.findByUserIdOrderByCreatedAtAsc(userId);
        List<CarbonAssessment> assessments = assessmentRepository.findByUserIdOrderByAssessmentDateDesc(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("history", historyList);

        if (assessments.isEmpty()) {
            result.put("trendDirection", "NO_DATA");
            result.put("percentageChange", BigDecimal.ZERO);
            result.put("currentEmissionsKg", BigDecimal.ZERO);
            result.put("baselineEmissionsKg", BigDecimal.ZERO);
            return result;
        }

        CarbonAssessment latest = assessments.get(0);
        CarbonAssessment oldest = assessments.get(assessments.size() - 1);

        BigDecimal currentKg = latest.getTotalFootprintKg();
        BigDecimal baselineKg = oldest.getTotalFootprintKg();

        BigDecimal diffKg = baselineKg.subtract(currentKg);
        BigDecimal pctChange = BigDecimal.ZERO;

        if (baselineKg.compareTo(BigDecimal.ZERO) > 0) {
            pctChange = diffKg.multiply(new BigDecimal("100")).divide(baselineKg, 2, RoundingMode.HALF_UP);
        }

        String trendDirection;
        if (pctChange.compareTo(BigDecimal.ZERO) > 0) {
            trendDirection = "DECREASING (IMPROVING)";
        } else if (pctChange.compareTo(BigDecimal.ZERO) < 0) {
            trendDirection = "INCREASING (REQUIRES ATTENTION)";
        } else {
            trendDirection = "STEADY (UNCHANGED)";
        }

        result.put("currentEmissionsKg", currentKg);
        result.put("baselineEmissionsKg", baselineKg);
        result.put("totalReducedKg", diffKg.max(BigDecimal.ZERO));
        result.put("percentageReduction", pctChange);
        result.put("trendDirection", trendDirection);
        result.put("assessmentCount", assessments.size());
        result.put("annualAvoidedTonnes", diffKg.multiply(new BigDecimal("12")).divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP).max(BigDecimal.ZERO));

        return result;
    }

    public String generateCsvReport(Long userId) {
        List<ProgressHistory> historyList = progressRepository.findByUserIdOrderByCreatedAtAsc(userId);
        StringBuilder csv = new StringBuilder();
        csv.append("Period,Total Footprint (kg CO2e),Transportation (kg),Energy (kg),Food (kg),Waste (kg),Shopping (kg)\n");

        for (ProgressHistory ph : historyList) {
            csv.append(ph.getPeriodLabel()).append(",")
               .append(ph.getTotalFootprintKg()).append(",")
               .append(ph.getTransportKg()).append(",")
               .append(ph.getEnergyKg()).append(",")
               .append(ph.getFoodKg()).append(",")
               .append(ph.getWasteKg()).append(",")
               .append(ph.getShoppingKg()).append("\n");
        }
        return csv.toString();
    }
}
