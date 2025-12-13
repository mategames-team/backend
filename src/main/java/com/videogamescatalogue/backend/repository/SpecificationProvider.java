package com.videogamescatalogue.backend.repository;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T,P> {
    String getKey();

    Specification<T> getSpecification(P param);
}
