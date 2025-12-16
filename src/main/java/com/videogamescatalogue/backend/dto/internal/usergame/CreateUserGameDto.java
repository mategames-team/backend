package com.videogamescatalogue.backend.dto.internal.usergame;

import com.videogamescatalogue.backend.model.UserGame;

public record CreateUserGameDto(
        Long apiId,
        UserGame.GameStatus status
) {
}
