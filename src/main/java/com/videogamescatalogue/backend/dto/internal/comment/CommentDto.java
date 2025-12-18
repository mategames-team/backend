package com.videogamescatalogue.backend.dto.internal.comment;

public record CommentDto(
        Long id,
        Long gameId,
        Long userId,
        String text,
        String localDateTime,
        Integer rating
) {
}
