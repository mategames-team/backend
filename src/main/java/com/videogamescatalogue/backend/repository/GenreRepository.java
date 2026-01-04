package com.videogamescatalogue.backend.repository;

import com.videogamescatalogue.backend.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
