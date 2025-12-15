package com.videogamescatalogue.backend.repository;

import com.videogamescatalogue.backend.model.UserGame;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    Optional<UserGame> findByUserIdAndGameApiId(Long userId, Long apiId);
}
