package com.videogamescatalogue.backend.repository.game;

import com.videogamescatalogue.backend.exception.SpecificationProviderNotFoundException;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.repository.SpecificationProvider;
import com.videogamescatalogue.backend.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GameSpecificationProviderManager
        implements SpecificationProviderManager<Game> {
    private final List<SpecificationProvider<Game, ?>> specificationProvidersList;

    @Override
    public SpecificationProvider<Game, ?> getSpecificationProvider(String key) {
        return specificationProvidersList.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(
                        () -> new SpecificationProviderNotFoundException(
                                "Can't find SpecificationProvider for key: "
                                        + key)
                );
    }
}
