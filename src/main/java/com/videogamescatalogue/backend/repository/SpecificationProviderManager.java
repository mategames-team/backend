package com.videogamescatalogue.backend.repository;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<T, ?> getSpecificationProvider(String key);
}
