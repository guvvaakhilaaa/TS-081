package com.ecoimpact.service;

import com.ecoimpact.dto.SequestrationRequestDto;
import com.ecoimpact.dto.SequestrationResultDto;
import com.ecoimpact.model.SequestrationEstimate;
import com.ecoimpact.model.User;
import com.ecoimpact.repository.SequestrationEstimateRepository;
import com.ecoimpact.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class SequestrationService {

    private final SequestrationEstimateRepository sequestrationRepository;
    private final UserRepository userRepository;

    // Molecular weight ratio CO2/C = 44 / 12 ≈ 3.6667
    private static final BigDecimal MOLECULAR_RATIO_CO2_TO_C = new BigDecimal("44")
            .divide(new BigDecimal("12"), 6, RoundingMode.HALF_UP);

    public SequestrationService(SequestrationEstimateRepository sequestrationRepository,
                                UserRepository userRepository) {
        this.sequestrationRepository = sequestrationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SequestrationResultDto calculateAndSave(SequestrationRequestDto req) {
        String method = req.getMethodType() != null ? req.getMethodType() : "BIOLOGICAL_TREE";
        BigDecimal qty = req.getQuantity() != null ? req.getQuantity() : new BigDecimal("25.00");
        BigDecimal cFraction = req.getCarbonFraction() != null ? req.getCarbonFraction() : new BigDecimal("0.5000");

        BigDecimal carbonStoredKg;
        BigDecimal uncertaintyPct;
        String formulaDesc;
        String methodNotes;

        switch (method) {
            case "BIOLOGICAL_TREE":
                // Average mature urban/forest tree absorbs approx 21.77 kg CO2 / year
                // Over treeAgeYears (e.g. 5 years) * tree count
                BigDecimal age = req.getTreeAgeYears() != null ? req.getTreeAgeYears() : new BigDecimal("5.00");
                BigDecimal annualRatePerTreeKg = new BigDecimal("21.77");
                BigDecimal co2Estimated = qty.multiply(age).multiply(annualRatePerTreeKg);
                carbonStoredKg = co2Estimated.divide(MOLECULAR_RATIO_CO2_TO_C, 2, RoundingMode.HALF_UP);
                uncertaintyPct = new BigDecimal("18.00");
                formulaDesc = "Tree Count (" + qty + ") × Age (" + age + " yrs) × 21.77 kg CO2/tree/yr (IPCC forestry baseline)";
                methodNotes = "Biological carbon stored in above-ground stem, branches, foliage, and root biomass. Subject to permanence risk and species growth climate curves.";
                break;

            case "SOIL_ORGANIC":
                // Soil Organic Carbon (SOC) enhancement via compost/regenerative practices
                // Qty = hectares or compost tons. Approx 0.35 tonnes C / ton compost
                carbonStoredKg = qty.multiply(new BigDecimal("350.00")); // kg C
                uncertaintyPct = new BigDecimal("25.00");
                formulaDesc = "Organic Input (tons) × 0.35 C Fraction × (44 ÷ 12)";
                methodNotes = "Enhanced soil microbial sequestration and humic carbon stabilization. Highly dependent on tillage depth and moisture retention.";
                break;

            case "BLUE_CARBON":
                // Coastal mangrove or wetland restoration. ~3.75 tonnes CO2/hectare/year
                carbonStoredKg = qty.multiply(new BigDecimal("1022.70"));
                uncertaintyPct = new BigDecimal("20.00");
                formulaDesc = "Coastal Hectares × 3,750 kg CO2 / 3.6667 (Carbon stored)";
                methodNotes = "Marine coastal sediment anaerobic sequestration with high multi-decadal carbon permanence.";
                break;

            case "TECH_DAC":
            default:
                // Direct Air Capture and Geological Mineralization
                carbonStoredKg = qty.divide(MOLECULAR_RATIO_CO2_TO_C, 2, RoundingMode.HALF_UP);
                uncertaintyPct = new BigDecimal("5.00");
                formulaDesc = "Engineered Solvent DAC Absorption: Pure captured CO2 (kg)";
                methodNotes = "Permanent geological basalt mineralization (e.g., CarbFix / Climeworks). Negligible reversal risk but high energy intensity.";
                break;
        }

        BigDecimal co2StoredKg = carbonStoredKg.multiply(MOLECULAR_RATIO_CO2_TO_C).setScale(2, RoundingMode.HALF_UP);
        BigDecimal co2StoredTonnes = co2StoredKg.divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP);

        // Save record if user exists
        User user = userRepository.findById(req.getUserId()).orElse(null);
        if (user != null) {
            SequestrationEstimate est = new SequestrationEstimate();
            est.setUser(user);
            est.setMethodType(method);
            est.setBiomassOrQuantity(qty);
            est.setCarbonFraction(cFraction);
            est.setCo2StoredKg(co2StoredKg);
            est.setUncertaintyPercentage(uncertaintyPct);
            est.setMethodologyNotes(methodNotes);
            sequestrationRepository.save(est);
        }

        SequestrationResultDto res = new SequestrationResultDto();
        res.setMethodType(method);
        res.setInputQuantity(qty);
        res.setCarbonStoredKg(carbonStoredKg.setScale(2, RoundingMode.HALF_UP));
        res.setCo2StoredKg(co2StoredKg);
        res.setCo2StoredTonnes(co2StoredTonnes);
        res.setUncertaintyPercentage(uncertaintyPct);
        res.setScientificFormula(formulaDesc);
        res.setMethodologyExplanation(methodNotes);
        res.setAccountingCaution("IMPORTANT: In accordance with GHG Protocol standards, carbon sequestration must be reported separately from operational gross emissions. It should never be subtracted directly to claim artificial 'net zero' status without accredited third-party verification.");

        return res;
    }

    public List<SequestrationEstimate> getUserEstimates(Long userId) {
        return sequestrationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
