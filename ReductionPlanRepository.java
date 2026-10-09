package com.ecoimpact.repository;

import com.ecoimpact.model.ReductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReductionPlanRepository extends JpaRepository<ReductionPlan, Long> {
    List<ReductionPlan> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<ReductionPlan> findFirstByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
}
