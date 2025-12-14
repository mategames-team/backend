package com.videogamescatalogue.backend.dto.internal.usergame;

public record UserGameDto(
        Long id,
        Long userid,
        Long gameApiId,
        String status
) {
}
