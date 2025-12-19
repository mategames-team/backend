package com.videogamescatalogue.backend.dto.internal.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateCommentRequestDto(
        @Size(max = 2000, message = "Comment must be less than 2000 digits")
        String text,
        @Min(0)
        @Max(5)
        Integer rating
) {
}
