package com.videogamescatalogue.backend.repository.game.spec;

import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.Platform;
import com.videogamescatalogue.backend.repository.SpecificationProvider;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GamePlatformsSpecificationProvider
        implements SpecificationProvider<Game, List<String>> {

    public static final String KEY = Game.SpecificationKey.PLATFORMS.getValue();

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Game> getSpecification(List<String> param) {
        return new Specification<Game>() {
            @Override
            public Predicate toPredicate(
                    Root<Game> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder criteriaBuilder
            ) {
                query.distinct(true);

                List<Platform.GeneralName> names = param.stream()
                        .map(String::toUpperCase)
                        .map(Platform.GeneralName::valueOf)
                        .toList();

                Join<Object, Object> platformJoin = root.join(KEY);

                return platformJoin.get("generalName").in(names);
            }
        };
    }
}
