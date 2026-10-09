package com.ecoimpact;

import com.ecoimpact.dto.HotspotResultDto;
import com.ecoimpact.model.CarbonAssessment;
import com.ecoimpact.model.Recommendation;
import com.ecoimpact.model.User;
import com.ecoimpact.model.UserPreference;
import com.ecoimpact.repository.CarbonAssessmentRepository;
import com.ecoimpact.repository.RecommendationRepository;
import com.ecoimpact.repository.UserPreferenceRepository;
import com.ecoimpact.repository.UserRepository;
import com.ecoimpact.service.HotspotAnalysisService;
import com.ecoimpact.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RecommendationServiceTest {

    private HotspotAnalysisService hotspotService;
    private CarbonAssessmentRepository assessmentRepository;
    private RecommendationRepository recommendationRepository;
    private UserPreferenceRepository userPreferenceRepository;
    private UserRepository userRepository;
    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        hotspotService = Mockito.mock(HotspotAnalysisService.class);
        assessmentRepository = Mockito.mock(CarbonAssessmentRepository.class);
        recommendationRepository = Mockito.mock(RecommendationRepository.class);
        userPreferenceRepository = Mockito.mock(UserPreferenceRepository.class);
        userRepository = Mockito.mock(UserRepository.class);

        recommendationService = new RecommendationService(
                hotspotService, assessmentRepository, recommendationRepository,
                userPreferenceRepository, userRepository
        );
    }

    @Test
    void testRecommendationGenerationAndWeightedRanking() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        CarbonAssessment assessment = new CarbonAssessment();
        assessment.setTotalFootprintKg(new BigDecimal("500.00"));
        when(assessmentRepository.findFirstByUserIdOrderByAssessmentDateDesc(1L)).thenReturn(Optional.of(assessment));

        when(userPreferenceRepository.findByUserId(1L)).thenReturn(Optional.of(new UserPreference()));

        HotspotResultDto hotspots = new HotspotResultDto();
        hotspots.setPrimaryHotspot("RESIDENTIAL");
        hotspots.setPrimaryHotspotPercentage(new BigDecimal("52.00"));
        hotspots.setRankedCategories(List.of(
                new HotspotResultDto.RankedCategoryDto("RESIDENTIAL", new BigDecimal("260.00"), new BigDecimal("52.00"), 1),
                new HotspotResultDto.RankedCategoryDto("TRANSPORTATION", new BigDecimal("140.00"), new BigDecimal("28.00"), 2)
        ));
        when(hotspotService.detectHotspots(1L)).thenReturn(hotspots);

        when(recommendationRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Recommendation> recommendations = recommendationService.generateAndRankRecommendations(1L);

        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty(), "Candidates must be generated for high hotspots");
        
        // Verify ranking score is computed and descending
        for (int i = 0; i < recommendations.size() - 1; i++) {
            assertTrue(recommendations.get(i).getRankingScore().compareTo(recommendations.get(i + 1).getRankingScore()) >= 0,
                    "Recommendations must be sorted descending by ranking score");
        }
    }
}
