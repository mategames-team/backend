package com.videogamescatalogue.backend.dto.internal.user;

import com.videogamescatalogue.backend.dto.internal.usergame.UserGameStatusDto;
import java.util.List;

public record UserResponseDto(
        Long id,
        String profileName,
        String about,
        String location,
        List<UserGameStatusDto> userGames
) {
}
