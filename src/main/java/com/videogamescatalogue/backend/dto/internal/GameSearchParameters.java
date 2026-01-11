package com.videogamescatalogue.backend.dto.internal;

import java.util.List;

public record GameSearchParameters(
        String name,
        List<Integer> years,
        List<String> platforms,
        List<String> genres
) {
}
