package com.videogamescatalogue.backend.service.game;

import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.dto.internal.game.GameWithStatusDto;
import com.videogamescatalogue.backend.model.User;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {
    void fetchBestGames();

    void fetchAndUpdateBestGames();

    void updateDbGames();

    Page<GameDto> getAllGamesFromDb(Pageable pageable);

    GameWithStatusDto getByApiId(Long id, User user);

    Page<GameDto> getAllGamesFromApi(Pageable pageable);

    Page<GameDto> search(GameSearchParameters searchParameters, Pageable pageable);

    Page<GameDto> apiSearch(Map<String, String> searchParams);
}
