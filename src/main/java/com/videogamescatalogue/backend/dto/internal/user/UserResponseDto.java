package com.videogamescatalogue.backend.dto.internal.user;

public record UserResponseDto(
        Long id,
        String profileName,
        String email
) {
}
