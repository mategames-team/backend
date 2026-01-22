package com.videogamescatalogue.backend.dto.internal.user;

import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
        String profileName,
        @Size(max = 5000, message = "About user info must be less than 5000 digits")
        String about,
        @Size(max = 50, message = "Location info must be less than 50 digits")
        String location
) {
}
