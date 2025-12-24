package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.service.game.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Tag(name = "Admin", description = "Admin management of games")
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class AdminGameController {
    private final GameService gameService;

    @Operation(
            summary = "Fetch best games from API",
            description = "Fetches best games from API and saves only new ones"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Games fetched successfully",
            content = @Content
    )
    @PostMapping("/fetch-best-games")
    public void fetchBestGamesManually() {
        log.info("Admin called fetch best games manually");
        gameService.fetchBestGames();
    }

    @Operation(
            summary = "Fetches best games from API and updates existing",
            description = "Fetches best games from API and updates existing ones"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Games retrieved successfully",
            content = @Content(schema = @Schema(implementation = GameDto.class))
    )
    @PostMapping("/fetch-update-best-games")
    public void fetchAndUpdateAllGamesManually() {
        log.info("Admin called fetch and update best games manually");
        gameService.fetchAndUpdateBestGames();
    }
}
