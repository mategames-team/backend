package com.videogamescatalogue.backend.dto.internal.comment;

public record CreateCommentRequestDto(
        String text,
        Integer rating
) {
}
