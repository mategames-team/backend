package com.videogamescatalogue.backend.service.game;

import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.dto.internal.game.GameWithStatusDto;
import com.videogamescatalogue.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {
    void fetchBestGames();

    void fetchAndUpdateBestGames();

    Page<GameDto> getAllGamesFromDb(Pageable pageable);

    GameWithStatusDto getByApiId(Long id, User user);

    Page<GameDto> getAllGamesFromApi(Pageable pageable);

    Page<GameDto> search(GameSearchParameters searchParameters, Pageable pageable);
}
