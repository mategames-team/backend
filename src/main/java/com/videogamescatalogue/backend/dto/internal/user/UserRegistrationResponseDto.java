package com.videogamescatalogue.backend.dto.internal.user;

public record UserRegistrationResponseDto(
        Long id,
        String profileName,
        String about,
        String location,
        String token
) {
}
