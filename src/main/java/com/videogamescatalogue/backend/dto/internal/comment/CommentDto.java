package com.videogamescatalogue.backend.dto.internal.comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        Long gameApiId,
        Long userId,
        String text,
        LocalDateTime localDateTime,
        Integer rating
) {
}
