package com.ecoimpact.repository;

import com.ecoimpact.model.ProgressHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressHistoryRepository extends JpaRepository<ProgressHistory, Long> {
    List<ProgressHistory> findByUserIdOrderByCreatedAtAsc(Long userId);
}
