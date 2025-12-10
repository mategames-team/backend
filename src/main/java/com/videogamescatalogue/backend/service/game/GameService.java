package com.videogamescatalogue.backend.service.game;

import com.videogamescatalogue.backend.dto.internal.GameDto;
import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {
    void fetchFromDb();

    Page<GameDto> getAllGamesFromDb(Pageable pageable);

    GameDto getFromDbByApiId(Long id);

    Page<GameDto> search(GameSearchParameters searchParameters, Pageable pageable);
}
