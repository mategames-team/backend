package com.videogamescatalogue.backend.dto.internal.comment;

public record CommentDto(
        Long id,
        Long gameApiId,
        Long userId,
        String text,
        String localDateTime,
        Integer rating
) {
}
