package com.videogamescatalogue.backend.repository;

import com.videogamescatalogue.backend.model.Platform;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Optional<Platform> findByGeneralName(String generalName);
}
