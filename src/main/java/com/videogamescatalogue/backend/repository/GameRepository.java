package com.videogamescatalogue.backend.repository;

import com.videogamescatalogue.backend.model.Game;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    Optional<Game> findByApiId(Long apiId);

    List<Game> findAllByApiIdIn(List<Long> apiIds);
}
