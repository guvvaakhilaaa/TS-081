package com.ecoimpact.repository;

import com.ecoimpact.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimulationScenarioRepository extends JpaRepository<SimulationScenario, Long> {
    List<SimulationScenario> findByUserIdOrderByCreatedAtDesc(Long userId);
}
