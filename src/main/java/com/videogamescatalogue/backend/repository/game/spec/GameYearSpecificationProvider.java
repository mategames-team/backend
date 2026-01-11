package com.videogamescatalogue.backend.repository.game.spec;

import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.repository.SpecificationProvider;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GameYearSpecificationProvider implements SpecificationProvider<Game, List<Integer>> {

    public static final String KEY = Game.SpecificationKey.YEARS.getValue();

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Game> getSpecification(List<Integer> years) {
        return new Specification<Game>() {
            @Override
            public Predicate toPredicate(
                    Root<Game> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder criteriaBuilder
            ) {

                return root.get("year").in(years);
            }
        };
    }
}
