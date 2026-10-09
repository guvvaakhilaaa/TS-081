package com.ecoimpact;

import com.ecoimpact.dto.HotspotResultDto;
import com.ecoimpact.model.CarbonAssessment;
import com.ecoimpact.model.CategoryEmission;
import com.ecoimpact.repository.CarbonAssessmentRepository;
import com.ecoimpact.repository.CategoryEmissionRepository;
import com.ecoimpact.service.HotspotAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HotspotAnalysisServiceTest {

    private CarbonAssessmentRepository assessmentRepository;
    private CategoryEmissionRepository categoryEmissionRepository;
    private HotspotAnalysisService hotspotService;

    @BeforeEach
    void setUp() {
        assessmentRepository = Mockito.mock(CarbonAssessmentRepository.class);
        categoryEmissionRepository = Mockito.mock(CategoryEmissionRepository.class);
        hotspotService = new HotspotAnalysisService(assessmentRepository, categoryEmissionRepository);
    }

    @Test
    void testHotspotSortingAndDetection() {
        CarbonAssessment assessment = new CarbonAssessment();
        assessment.setId(10L);
        when(assessmentRepository.findFirstByUserIdOrderByAssessmentDateDesc(1L)).thenReturn(Optional.of(assessment));

        CategoryEmission c1 = new CategoryEmission(assessment, "TRANSPORTATION", new BigDecimal("120.00"), new BigDecimal("24.00"));
        CategoryEmission c2 = new CategoryEmission(assessment, "RESIDENTIAL", new BigDecimal("250.00"), new BigDecimal("50.00"));
        CategoryEmission c3 = new CategoryEmission(assessment, "FOOD", new BigDecimal("130.00"), new BigDecimal("26.00"));

        when(categoryEmissionRepository.findByCarbonAssessmentId(10L)).thenReturn(List.of(c1, c2, c3));

        HotspotResultDto result = hotspotService.detectHotspots(1L);

        assertNotNull(result);
        assertEquals("RESIDENTIAL", result.getPrimaryHotspot(), "Largest emission source must be detected as primary hotspot");
        assertEquals(new BigDecimal("50.00"), result.getPrimaryHotspotPercentage());
        assertEquals("FOOD", result.getSecondaryHotspot(), "Second largest contributor must be detected as secondary hotspot");
        assertEquals(3, result.getRankedCategories().size());
        assertEquals("RESIDENTIAL", result.getRankedCategories().get(0).getCategory());
        assertEquals(1, result.getRankedCategories().get(0).getRank());
    }
}
