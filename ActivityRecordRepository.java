package com.ecoimpact.repository;

import com.ecoimpact.model.ActivityRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRecordRepository extends JpaRepository<ActivityRecord, Long> {
    List<ActivityRecord> findByUserIdOrderByRecordDateDesc(Long userId);
    Optional<ActivityRecord> findFirstByUserIdOrderByRecordDateDesc(Long userId);
}
