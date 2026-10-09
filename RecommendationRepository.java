package com.ecoimpact.repository;

import com.ecoimpact.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserIdOrderByRankingScoreDesc(Long userId);
    List<Recommendation> findByUserIdAndStatus(Long userId, String status);
}
