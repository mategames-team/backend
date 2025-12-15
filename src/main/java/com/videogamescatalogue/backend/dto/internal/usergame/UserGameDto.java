package com.videogamescatalogue.backend.dto.internal.usergame;

public record UserGameDto(
        Long id,
        Long userId,
        Long gameId,
        Long gameApiId,
        String status
) {
}
