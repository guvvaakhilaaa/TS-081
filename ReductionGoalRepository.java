package com.ecoimpact.repository;

import com.ecoimpact.model.ReductionGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReductionGoalRepository extends JpaRepository<ReductionGoal, Long> {
    List<ReductionGoal> findByUserIdOrderByDeadlineAsc(Long userId);
}
