package com.videogamescatalogue.backend.repository;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T,P> {
    String getKey();

    Specification<T> getSpecification(P param);
}
