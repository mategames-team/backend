package com.videogamescatalogue.backend.repository;

import com.videogamescatalogue.backend.model.Developer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findByApiId(Long apiId);

    List<Developer> findAllByApiIdIn(List<Long> apiIds);
}
