package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.GameDto;
import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.model.Platform;
import com.videogamescatalogue.backend.service.game.GameService;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/games")
@RestController
public class GameController {
    public static final int DEFAULT_PAGE_SIZE = 30;
    private final GameService gameService;

    @GetMapping("/local")
    public Page<GameDto> getAllGamesFromDb(
            @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "apiRating",
                    direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return gameService.getAllGamesFromDb(pageable);
    }

    @GetMapping("/local/id/{id}")
    public GameDto getFromDbByApiId(@PathVariable Long id) {
        return gameService.getFromDbByApiId(id);
    }

    @GetMapping("/local/search")
    public Page<GameDto> search(
            @ModelAttribute GameSearchParameters searchParameters,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        validateSearchParams(searchParameters);
        return gameService.search(searchParameters, pageable);
    }

    private void validateSearchParams(GameSearchParameters searchParameters) {
        if (searchParameters.platforms() != null) {
            try {
                searchParameters.platforms()
                        .forEach(
                                p -> Platform.GeneralName.valueOf(p.toUpperCase())
                        );
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Invalid platforms provided. Valid platforms: "
                        + Arrays.stream(Platform.GeneralName.values())
                                .map(Enum::toString)
                                .collect(Collectors.joining(", ")), e);
            }
        }
        if (searchParameters.genres() != null) {
            try {
                searchParameters.genres().forEach(g -> Genre.Name.valueOf(g.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid genres provided. Valid genres: "
                + Arrays.stream(Genre.Name.values())
                        .map(Enum::toString)
                        .collect(Collectors.joining(", ")), e);
            }
        }
    }
}
