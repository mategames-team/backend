package com.videogamescatalogue.backend.repository.game;

import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.repository.SpecificationBuilder;
import com.videogamescatalogue.backend.repository.SpecificationProvider;
import com.videogamescatalogue.backend.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameSpecificationBuilder
        implements SpecificationBuilder<Game, GameSearchParameters> {
    private final SpecificationProviderManager<Game> specificationProviderManager;

    @Override
    public Specification<Game> build(GameSearchParameters searchParameters) {
        if (searchParameters == null) {
            throw new IllegalArgumentException("Search Parameters cannot be null");
        }
        Specification<Game> specification = Specification.where((Specification<Game>) null);
        specification = getSpecificationForParam(
                searchParameters.name(),
                Game.SpecificationKey.NAME.getValue(), specification
        );
        specification = getSpecificationForParam(
                searchParameters.year(),
                Game.SpecificationKey.YEAR.getValue(), specification
        );
        specification = getSpecificationForParam(
                searchParameters.platforms(),
                Game.SpecificationKey.PLATFORMS.getValue(), specification
        );
        specification = getSpecificationForParam(
                searchParameters.genres(),
                Game.SpecificationKey.GENRES.getValue(), specification
        );

        return specification;
    }

    @SuppressWarnings("unchecked")
    private <T> Specification<Game> getSpecificationForParam(
            T searchParameter,
            String key,
            Specification<Game> specification
    ) {
        if (searchParameter != null) {
            SpecificationProvider<Game, Object> provider =
                    (SpecificationProvider<Game, Object>) specificationProviderManager
                            .getSpecificationProvider(key);
            return specification.and(
                    provider.getSpecification(searchParameter)
            );
        }
        return specification;
    }
}
