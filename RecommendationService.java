package com.ecoimpact.service;

import com.ecoimpact.dto.HotspotResultDto;
import com.ecoimpact.model.CarbonAssessment;
import com.ecoimpact.model.Recommendation;
import com.ecoimpact.model.User;
import com.ecoimpact.model.UserPreference;
import com.ecoimpact.repository.CarbonAssessmentRepository;
import com.ecoimpact.repository.RecommendationRepository;
import com.ecoimpact.repository.UserPreferenceRepository;
import com.ecoimpact.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class RecommendationService {

    private final HotspotAnalysisService hotspotService;
    private final CarbonAssessmentRepository assessmentRepository;
    private final RecommendationRepository recommendationRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;

    // Documented scoring weights (Algorithm 2)
    private static final BigDecimal W1_IMPACT = new BigDecimal("0.40");
    private static final BigDecimal W2_FEASIBILITY = new BigDecimal("0.25");
    private static final BigDecimal W3_PREFERENCE = new BigDecimal("0.20");
    private static final BigDecimal W4_AFFORDABILITY = new BigDecimal("0.15");

    public RecommendationService(HotspotAnalysisService hotspotService,
                                 CarbonAssessmentRepository assessmentRepository,
                                 RecommendationRepository recommendationRepository,
                                 UserPreferenceRepository userPreferenceRepository,
                                 UserRepository userRepository) {
        this.hotspotService = hotspotService;
        this.assessmentRepository = assessmentRepository;
        this.recommendationRepository = recommendationRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Algorithm 1 & Algorithm 2: Rule-Based & Weighted Recommendation Ranking
     */
    @Transactional
    public List<Recommendation> generateAndRankRecommendations(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return Collections.emptyList();

        HotspotResultDto hotspots = hotspotService.detectHotspots(userId);
        Optional<CarbonAssessment> assessmentOpt = assessmentRepository.findFirstByUserIdOrderByAssessmentDateDesc(userId);
        if (assessmentOpt.isEmpty()) {
            return recommendationRepository.findByUserIdOrderByRankingScoreDesc(userId);
        }

        CarbonAssessment assessment = assessmentOpt.get();
        BigDecimal totalFootprint = assessment.getTotalFootprintKg();

        UserPreference pref = userPreferenceRepository.findByUserId(userId).orElse(new UserPreference());

        // 1. Clear previous suggested recommendations to keep suggestions freshly updated
        List<Recommendation> existingSuggested = recommendationRepository.findByUserIdAndStatus(userId, "SUGGESTED");
        recommendationRepository.deleteAll(existingSuggested);

        List<Recommendation> candidatePool = new ArrayList<>();

        // 2. Rule-Based Candidate Generation Matching Hotspots
        for (HotspotResultDto.RankedCategoryDto rankedCat : hotspots.getRankedCategories()) {
            String category = rankedCat.getCategory();
            BigDecimal catEmissions = rankedCat.getEmissionsKg();

            if (category.equals("RESIDENTIAL") && catEmissions.compareTo(new BigDecimal("50")) > 0) {
                // Rule: If residential is high, suggest LED / smart power strips and AC temp
                candidatePool.add(createRecommendation(
                        user, "RESIDENTIAL",
                        "Optimize AC Temperature to 24°C & Smart Strip Control",
                        "Setting your air conditioner thermostat from 20°C to 24°C saves up to 24% of cooling energy, while smart power strips prevent 10-15 kWh of monthly phantom drain.",
                        "Residential energy accounts for " + rankedCat.getPercentage() + "% of your carbon output.",
                        catEmissions.multiply(new BigDecimal("0.22")), // 22% reduction in residential
                        new BigDecimal("22.50"), // $22.50 monthly savings
                        "LOW", "Immediate (1 day)",
                        "1. Adjust AC thermostat to 24°C or above.\n2. Turn off appliances at plug points when not in use.\n3. Utilize natural daylight during afternoon hours.",
                        new BigDecimal("0.85"), new BigDecimal("0.90"), new BigDecimal("0.95") // feasibility, preference, affordability
                ));

                candidatePool.add(createRecommendation(
                        user, "RESIDENTIAL",
                        "Adopt 5-Star Inverter Appliances & Rooftop Solar",
                        "Upgrading to BEE 5-Star inverter appliances or installing a grid-tied residential solar rooftop generates clean energy directly on-site.",
                        "Eliminates reliance on fossil-fuel heavy national grid electricity (" + rankedCat.getPercentage() + "% contribution).",
                        catEmissions.multiply(new BigDecimal("0.45")),
                        new BigDecimal("48.00"),
                        "HIGH", "1-2 months",
                        "1. Audit domestic energy meter consumption.\n2. Replace older halogen bulbs with Philips LED.\n3. Explore local net-metering solar subsidy scheme.",
                        new BigDecimal("0.60"), new BigDecimal("0.70"), new BigDecimal("0.50")
                ));
            }

            if (category.equals("TRANSPORTATION") && catEmissions.compareTo(new BigDecimal("30")) > 0) {
                // Rule: If transport is a major contributor, suggest public transit & carpooling
                candidatePool.add(createRecommendation(
                        user, "TRANSPORTATION",
                        "Modal Shift: Metro & Public Transit 3 Days/Week",
                        "Shifting 3 commute days per week from a private petrol vehicle to suburban electric metro or public bus transit reduces personal travel emissions dramatically.",
                        "Transport emissions comprise " + rankedCat.getPercentage() + "% of your monthly carbon profile.",
                        catEmissions.multiply(new BigDecimal("0.35")),
                        new BigDecimal("35.00"),
                        "LOW", "1 week",
                        "1. Identify the nearest metro or bus transit station route.\n2. Purchase a rechargeable monthly smart transit card.\n3. Combine errands into batch trips.",
                        new BigDecimal("0.80"), new BigDecimal("0.85"), new BigDecimal("0.90")
                ));

                candidatePool.add(createRecommendation(
                        user, "TRANSPORTATION",
                        "Eco-Driving Habits & Tire Pressure Optimization",
                        "Maintaining correct tire pressure, gentle acceleration, and minimizing idle combustion improves fuel economy by 12-15%.",
                        "Direct reduction in fuel consumption for private vehicle trips.",
                        catEmissions.multiply(new BigDecimal("0.14")),
                        new BigDecimal("16.00"),
                        "LOW", "Immediate",
                        "1. Check tire inflation pressure bi-weekly.\n2. Switch off engine during long traffic signal waits (>20 sec).\n3. Maintain steady speeds around 45-60 km/h.",
                        new BigDecimal("0.95"), new BigDecimal("0.90"), new BigDecimal("1.00")
                ));
            }

            if (category.equals("FOOD") && catEmissions.compareTo(new BigDecimal("40")) > 0) {
                // Rule: If food emissions are significant, suggest meat-free days & food waste reduction
                candidatePool.add(createRecommendation(
                        user, "FOOD",
                        "Adopt 'Green Plant-Rich Dining' 3 Days/Week",
                        "Incorporating plant-rich vegetarian meals just 3 days a week cuts dietary greenhouse emissions (methane and nitrous oxide from livestock supply chain).",
                        "Food and diet is your #" + rankedCat.getRank() + " emission source (" + rankedCat.getPercentage() + "%).",
                        catEmissions.multiply(new BigDecimal("0.25")),
                        new BigDecimal("18.00"),
                        "MEDIUM", "Immediate",
                        "1. Plan weekly grocery lists around lentils, legumes, tofu, and seasonal veggies.\n2. Explore delicious plant-based recipes.\n3. Support local farmers' markets.",
                        new BigDecimal("0.85"), new BigDecimal("0.80"), new BigDecimal("0.90")
                ));

                candidatePool.add(createRecommendation(
                        user, "FOOD",
                        "Zero-Waste Meal Prepping & Airtight Storage",
                        "Planning portions, freezing perishable leftovers, and monitoring expiry dates eliminates household food waste and associated landfill methane.",
                        "Food waste generates 2.5 kg CO2e per kg discarded to landfills.",
                        new BigDecimal("18.50"),
                        new BigDecimal("25.00"),
                        "LOW", "Immediate",
                        "1. Shop with a strict grocery shopping checklist.\n2. Keep older items at the front of the refrigerator ('first in, first out').\n3. Turn leftover vegetables into stocks and soups.",
                        new BigDecimal("0.90"), new BigDecimal("0.95"), new BigDecimal("1.00")
                ));
            }

            if (category.equals("SHOPPING") && catEmissions.compareTo(new BigDecimal("20")) > 0) {
                candidatePool.add(createRecommendation(
                        user, "SHOPPING",
                        "Slow Fashion & 30-Day Purchase Consideration Rule",
                        "Pausing 30 days before buying non-essential apparel or gadgets reduces impulse purchases and avoids embodied production footprint.",
                        "Retail goods and textiles account for " + rankedCat.getPercentage() + "% of footprint.",
                        catEmissions.multiply(new BigDecimal("0.30")),
                        new BigDecimal("40.00"),
                        "LOW", "Ongoing",
                        "1. Wait 30 days before checkout on non-essential online carts.\n2. Choose durable, repairable items.\n3. Donate or repair existing clothing.",
                        new BigDecimal("0.90"), new BigDecimal("0.85"), new BigDecimal("1.00")
                ));
            }

            if (category.equals("WASTE") && catEmissions.compareTo(new BigDecimal("3")) > 0) {
                candidatePool.add(createRecommendation(
                        user, "WASTE",
                        "Household Source Segregation & Aerobic Composting",
                        "Separating organic kitchen scraps for balcony aerobic composting diverts organics from municipal landfills and eliminates anaerobic methane.",
                        "Waste is ranked #" + rankedCat.getRank() + " in your footprint.",
                        catEmissions.multiply(new BigDecimal("0.50")),
                        new BigDecimal("5.00"),
                        "LOW", "1 week",
                        "1. Keep dual bins: green for wet organics, blue for dry recyclables.\n2. Use a ventilated terracotta or bucket composter.\n3. Use nutrient-rich compost for indoor potted plants.",
                        new BigDecimal("0.85"), new BigDecimal("0.80"), new BigDecimal("0.95")
                ));
            }
        }

        // 3. Algorithm 2: Weighted Recommendation Scoring & Ranking
        // Score = (w1 * Normalized Impact) + (w2 * Feasibility) + (w3 * Preference Match) + (w4 * Affordability)
        BigDecimal maxReduction = candidatePool.stream()
                .map(Recommendation::getEstimatedReductionKg)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal("50.00"));

        if (maxReduction.compareTo(BigDecimal.ZERO) == 0) maxReduction = BigDecimal.ONE;

        for (Recommendation rec : candidatePool) {
            BigDecimal normImpact = rec.getEstimatedReductionKg().divide(maxReduction, 4, RoundingMode.HALF_UP);
            
            // Extract feasibility/preference/affordability weights assigned in helper or default
            BigDecimal feasibility = rec.getDifficultyLevel().equals("LOW") ? new BigDecimal("0.95") :
                    (rec.getDifficultyLevel().equals("MEDIUM") ? new BigDecimal("0.80") : new BigDecimal("0.60"));
            
            BigDecimal prefMatch = new BigDecimal("0.85");
            if (pref.getPrimaryTransport() != null && rec.getCategory().equals("TRANSPORTATION")) {
                prefMatch = new BigDecimal("0.90");
            }

            BigDecimal affordability = rec.getPotentialCostSavingsUsd().compareTo(new BigDecimal("20.00")) >= 0 ?
                    new BigDecimal("0.95") : new BigDecimal("0.80");

            BigDecimal score = (W1_IMPACT.multiply(normImpact))
                    .add(W2_FEASIBILITY.multiply(feasibility))
                    .add(W3_PREFERENCE.multiply(prefMatch))
                    .add(W4_AFFORDABILITY.multiply(affordability))
                    .setScale(4, RoundingMode.HALF_UP);

            rec.setRankingScore(score);
        }

        // Sort descending by calculated score
        candidatePool.sort(Comparator.comparing(Recommendation::getRankingScore).reversed());

        return recommendationRepository.saveAll(candidatePool);
    }

    public List<Recommendation> getRecommendations(Long userId) {
        List<Recommendation> list = recommendationRepository.findByUserIdOrderByRankingScoreDesc(userId);
        if (list.isEmpty()) {
            return generateAndRankRecommendations(userId);
        }
        return list;
    }

    @Transactional
    public Recommendation updateStatus(Long recommendationId, String newStatus) {
        Recommendation rec = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found: " + recommendationId));
        rec.setStatus(newStatus);
        return recommendationRepository.save(rec);
    }

    private Recommendation createRecommendation(User user, String category, String title, String desc,
                                                 String reasoning, BigDecimal reductionKg, BigDecimal savingsUsd,
                                                 String difficulty, String time, String steps,
                                                 BigDecimal feas, BigDecimal pref, BigDecimal afford) {
        Recommendation r = new Recommendation();
        r.setUser(user);
        r.setCategory(category);
        r.setTitle(title);
        r.setDescription(desc);
        r.setReasoning(reasoning);
        r.setEstimatedReductionKg(reductionKg.setScale(2, RoundingMode.HALF_UP));
        r.setPotentialCostSavingsUsd(savingsUsd.setScale(2, RoundingMode.HALF_UP));
        r.setDifficultyLevel(difficulty);
        r.setImplementationTime(time);
        r.setActionSteps(steps);
        r.setRankingScore(BigDecimal.ZERO);
        r.setStatus("SUGGESTED");
        return r;
    }
}
