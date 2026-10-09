package com.ecoimpact.repository;

import com.ecoimpact.model.CarbonAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarbonAssessmentRepository extends JpaRepository<CarbonAssessment, Long> {
    List<CarbonAssessment> findByUserIdOrderByAssessmentDateDesc(Long userId);
    Optional<CarbonAssessment> findFirstByUserIdOrderByAssessmentDateDesc(Long userId);
}
