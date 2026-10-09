package com.ecoimpact.repository;

import com.ecoimpact.model.CategoryEmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryEmissionRepository extends JpaRepository<CategoryEmission, Long> {
    List<CategoryEmission> findByCarbonAssessmentId(Long assessmentId);
}
