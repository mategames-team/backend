package com.videogamescatalogue.backend.dto.internal.user;

public record UserLoginResponseDto(
        String token,
        Long userId
) {
}
