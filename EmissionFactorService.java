package com.ecoimpact.service;

import com.ecoimpact.model.EmissionFactor;
import com.ecoimpact.repository.EmissionFactorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class EmissionFactorService {

    private final EmissionFactorRepository emissionFactorRepository;

    public EmissionFactorService(EmissionFactorRepository emissionFactorRepository) {
        this.emissionFactorRepository = emissionFactorRepository;
    }

    public List<EmissionFactor> getAllFactors() {
        return emissionFactorRepository.findAll();
    }

    public List<EmissionFactor> getFactorsByCategory(String category) {
        return emissionFactorRepository.findByCategory(category);
    }

    public BigDecimal getFactorValue(String category, String activityType, BigDecimal fallback) {
        Optional<EmissionFactor> factor = emissionFactorRepository.findByCategoryAndActivityType(category, activityType);
        return factor.map(EmissionFactor::getFactorValue).orElse(fallback);
    }

    public Optional<EmissionFactor> getFactor(String category, String activityType) {
        return emissionFactorRepository.findByCategoryAndActivityType(category, activityType);
    }
}
