package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.usergame.CreateUserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.model.UserGame;
import com.videogamescatalogue.backend.service.usergame.UserGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Games", description = "Manage games in the authenticated user's library")
@RequiredArgsConstructor
@RequestMapping("/user-games")
@RestController
public class UserGameController {
    public static final int DEFAULT_PAGE_SIZE = 30;
    private final UserGameService userGameService;

    @Operation(
            summary = "Add or update a game in user's library",
            description = """
          Creates or updates a game entry in the authenticated user's library.

            Behaviour:
            - If the game already exists in the user's library → its status is updated
            - If the game does not exist → a new entry is created

            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "User game created or updated successfully",
            content = @Content(
                    schema = @Schema(implementation = UserGameDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authenticated",
            content = @Content
    )
    @PostMapping
    public UserGameDto createOrUpdate(
            @RequestBody CreateUserGameDto createDto,
            @AuthenticationPrincipal User user
    ) {
        return userGameService.createOrUpdate(createDto, user);
    }

    @Operation(
            summary = "Remove a game from user's library",
            description = """
            Deletes a game entry from the authenticated user's library.

            Only the owner of the entry can delete it.
            """
    )
    @ApiResponse(
            responseCode = "204",
            description = "User game deleted successfully"
    )
    @ApiResponse(
            responseCode = "403",
            description = "User is not allowed to delete this entry",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "User game not found",
            content = @Content
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        userGameService.delete(id, user);
    }

    @Operation(
            summary = "Get user's games by status",
            description = """
    Returns paginated games from the authenticated 
    user's library filtered by game status
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "User games retrieved successfully",
            content = @Content(
                    schema = @Schema(implementation = UserGameDto.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authenticated",
            content = @Content
    )
    @GetMapping
    public Page<UserGameDto> getByStatus(
            @RequestParam UserGame.GameStatus status,
            @RequestParam(required = false) Long userId,
            @AuthenticationPrincipal User authenticatedUser,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        return userGameService.getByStatus(
                status, userId, authenticatedUser, pageable
        );
    }
}
