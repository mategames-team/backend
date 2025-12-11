package com.videogamescatalogue.backend.dto.external;

import java.util.List;

public record ApiResponseGames(
        String next,
        List<ApiResponseGameDto> results
) {
}
