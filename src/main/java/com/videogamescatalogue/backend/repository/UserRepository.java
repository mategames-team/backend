package com.videogamescatalogue.backend.repository;

import com.videogamescatalogue.backend.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByProfileNameIgnoreCase(String profileName);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);
}
