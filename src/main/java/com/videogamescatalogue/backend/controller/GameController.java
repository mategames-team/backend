package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.GameDto;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.model.Platform;
import com.videogamescatalogue.backend.service.GameService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/games")
@RestController
public class GameController {
    private final GameService gameService;

    @GetMapping("/local")
    public Page<GameDto> getAllGamesFromDb(
            @PageableDefault(size = 30, sort = "apiRating",
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
            @PageableDefault(size = 30)
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
            @PageableDefault(size = 30)
            Pageable pageable
    ) {
        return gameService.getByYear(year, pageable);
    }

    @GetMapping("/local/platform/{platform}")
    public ResponseEntity<?> getFromDbByPlatform(
            @PathVariable String platform,
            @PageableDefault(size = 30)
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
}
