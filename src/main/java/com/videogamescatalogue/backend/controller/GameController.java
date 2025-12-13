package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.GameDto;
import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.model.Platform;
import com.videogamescatalogue.backend.service.GameService;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
    public GameDto getFromDbById(@PathVariable Long id) {
        return gameService.getFromDbById(id);
    }

    @GetMapping("/local/genre/{genre}")
    public ResponseEntity<?> getFromDbByGenre(
            @PathVariable String genre,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        Genre.Name name;
        try {
            name = Genre.Name.valueOf(genre.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body("There is no genre by name: " + genre
                            + ". Allowed values: " + Arrays.asList(Genre.Name.values())
                    );
        }
        return ResponseEntity.ok(gameService.getFromDbByGenre(name, pageable));
    }

    @GetMapping("/local/year/{year}")
    public Page<GameDto> getFromDbByYear(
            @PathVariable int year,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        return gameService.getByYear(year, pageable);
    }

    @GetMapping("/local/platform/{platform}")
    public ResponseEntity<?> getFromDbByPlatform(
            @PathVariable String platform,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        Platform.GeneralName name;
        try {
            name = Platform.GeneralName.valueOf(platform.toUpperCase());

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body("There is no platform by name: " + platform
                    + ". Allowed values: " + Arrays.asList(Platform.GeneralName.values())
                    );
        }
        return ResponseEntity.ok(gameService.getFromDbByPlatform(name, pageable));
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
