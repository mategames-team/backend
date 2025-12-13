package com.videogamescatalogue.backend.repository;

import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.model.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    Page<Game> findByGenresName(Genre.Name genre, Pageable pageable);

    Page<Game> findByYear(int year, Pageable pageable);

    Page<Game> findByPlatformsGeneralName(Platform.GeneralName generalName, Pageable pageable);
}
