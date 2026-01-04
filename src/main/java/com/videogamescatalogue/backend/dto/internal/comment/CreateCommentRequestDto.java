package com.videogamescatalogue.backend.dto.internal.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentRequestDto(
        @Size(max = 2000, message = "Comment must be less than 2000 digits")
        String text,
        @NotNull
        @Min(0)
        @Max(10)
        Integer rating
) {
}
