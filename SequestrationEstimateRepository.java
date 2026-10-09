package com.ecoimpact.repository;

import com.ecoimpact.model.SequestrationEstimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SequestrationEstimateRepository extends JpaRepository<SequestrationEstimate, Long> {
    List<SequestrationEstimate> findByUserIdOrderByCreatedAtDesc(Long userId);
}
