package com.videogamescatalogue.backend.repository;

import com.videogamescatalogue.backend.model.UserGame;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    Optional<UserGame> findByUserIdAndGameApiId(Long userId, Long apiId);

    List<UserGame> findAllByUserId(Long userId);

    Page<UserGame> findByUserIdAndStatus(
            Long userId, UserGame.GameStatus status, Pageable pageable
    );
}
