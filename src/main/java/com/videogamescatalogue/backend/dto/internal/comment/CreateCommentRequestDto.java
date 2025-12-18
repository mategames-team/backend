package com.videogamescatalogue.backend.dto.internal.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequestDto(
        String text,
        @NotNull
        @Min(0)
        @Max(3)
        Integer rating
) {
}
