package com.videogamescatalogue.backend.dto.internal.usergame;

import com.videogamescatalogue.backend.dto.internal.game.GameDto;

public record UserGameDto(
        Long id,
        Long userId,
        GameDto gameDto,
        String status
) {
}
