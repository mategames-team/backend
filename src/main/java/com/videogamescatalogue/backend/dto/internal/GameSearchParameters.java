package com.videogamescatalogue.backend.dto.internal;

import java.util.List;

public record GameSearchParameters(
        String name,
        Integer year,
        List<String> platforms,
        List<String> genres
) {
}
