package com.videogamescatalogue.backend.service;

import com.videogamescatalogue.backend.dto.internal.GameDto;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.model.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {
    void fetchFromDb();

    Page<GameDto> getAllGamesFromDb(Pageable pageable);

    GameDto getFromDbById(Long id);

    Page<GameDto> getFromDbByGenre(Genre.Name genre, Pageable pageable);

    Page<GameDto> getByYear(int year, Pageable pageable);

    Page<GameDto> getFromDbByPlatform(Platform.GeneralName platform, Pageable pageable);
}
