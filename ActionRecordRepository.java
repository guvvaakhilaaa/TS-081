package com.ecoimpact.repository;

import com.ecoimpact.model.ActionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRecordRepository extends JpaRepository<ActionRecord, Long> {
    List<ActionRecord> findByUserIdOrderByCompletedDateDesc(Long userId);
}
