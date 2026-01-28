package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.dto.internal.game.GameWithStatusDto;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.model.Platform;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.service.game.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Games", description = "Retrieve and search video games")
@RequiredArgsConstructor
@RequestMapping("/games")
@RestController
public class GameController {
    public static final int DEFAULT_PAGE_SIZE = 30;
    public static final int DEFAULT_PAGE_NUMBER = 1;
    private final GameService gameService;

    @Operation(
            summary = "Get games stored in local database",
            description = """
    Returns paginated games from the local database 
    sorted by API rating starting from the highest
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Games retrieved successfully",
            content = @Content(schema = @Schema(implementation = GameDto.class))
    )
    @GetMapping("/local")
    public Page<GameDto> getAllGamesFromDb(
            @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "apiRating",
                    direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return gameService.getAllGamesFromDb(pageable);
    }

    @Operation(
            summary = "Get specific game and its details by API ID",
            description = """
        Returns detailed game information using a hybrid DB + external API strategy.

        Behaviour:
        - If the game exists in the local database:
            - If it already has a description → data is returned from DB
            - If description is missing → full data is fetched from external API,
              saved to DB, and then returned
        - If the game does not exist in the database:
            - Data is fetched from external API and returned without saving to DB

        If the user is authenticated, the response also includes
        user-specific game status.
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Game retrieved successfully",
            content = @Content(schema = @Schema(implementation = GameWithStatusDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Game not found",
            content = @Content
    )
    @GetMapping("{gameApiId}")
    public GameWithStatusDto getByApiId(
            @PathVariable Long gameApiId,
            @AuthenticationPrincipal User user
    ) {
        return gameService.getByApiId(gameApiId, user);
    }

    @Operation(
            summary = "Get games from external API",
            description = "Returns paginated games fetched from the external games API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Games retrieved successfully",
            content = @Content(schema = @Schema(implementation = GameDto.class))
    )
    @GetMapping
    public Page<GameDto> getAllGamesFromApi(
            @PageableDefault(size = DEFAULT_PAGE_SIZE, page = DEFAULT_PAGE_NUMBER)
            Pageable pageable
    ) {
        return gameService.getAllGamesFromApi(pageable);
    }

    @Operation(
            summary = "Search games in local database",
            description = "Search games by platforms, genres, name and year"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search results returned successfully",
            content = @Content(schema = @Schema(implementation = GameDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid search parameters",
            content = @Content
    )
    @GetMapping("/local/search")
    public Page<GameDto> search(
            @ModelAttribute GameSearchParameters searchParameters,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        return gameService.search(searchParameters, pageable);
    }

    @Operation(
            summary = "Search games via external API",
            description = "Flexible search using raw query parameters forwarded to the external API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search results returned successfully",
            content = @Content(schema = @Schema(implementation = GameDto.class))
    )
    @GetMapping("/search")
    public Page<GameDto> apiSearch(
            @RequestParam Map<String, String> searchParams
    ) {
        return gameService.apiSearch(searchParams);
    }
}
