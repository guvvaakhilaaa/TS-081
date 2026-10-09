package com.ecoimpact.repository;

import com.ecoimpact.model.EmissionFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmissionFactorRepository extends JpaRepository<EmissionFactor, Long> {
    List<EmissionFactor> findByCategory(String category);
    Optional<EmissionFactor> findByCategoryAndActivityType(String category, String activityType);
    List<EmissionFactor> findByCountryRegion(String countryRegion);
}
