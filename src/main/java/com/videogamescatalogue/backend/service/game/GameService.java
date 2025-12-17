package com.videogamescatalogue.backend.service.game;

import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {
    void fetchBestGames();

    void fetchAndUpdateBestGames();

    Page<GameDto> getAllGamesFromDb(Pageable pageable);

    GameDto getByApiId(Long id);

    Page<GameDto> getAllGamesFromApi(Pageable pageable);

    Page<GameDto> search(GameSearchParameters searchParameters, Pageable pageable);
}
