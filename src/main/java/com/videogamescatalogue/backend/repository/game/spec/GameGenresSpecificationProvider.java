package com.videogamescatalogue.backend.repository.game.spec;

import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.Genre;
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
public class GameGenresSpecificationProvider implements SpecificationProvider<Game, List<String>> {

    public static final String KEY = Game.SpecificationKey.GENRES.getValue();

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

                List<Genre.Name> names = param.stream()
                        .map(String::toUpperCase)
                        .map(Genre.Name::valueOf)
                        .toList();

                Join<Game, Genre> genreJoin = root.join(KEY);

                return genreJoin.get("name").in(names);
            }
        };
    }
}
