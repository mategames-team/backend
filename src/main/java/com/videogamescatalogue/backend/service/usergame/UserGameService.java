package com.videogamescatalogue.backend.service.usergame;

import com.videogamescatalogue.backend.dto.internal.usergame.CreateUserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.model.UserGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserGameService {
    UserGameDto createOrUpdate(CreateUserGameDto createDto, User user);

    void delete(Long apiId, User user);

    Page<UserGameDto> getByStatus(
            UserGame.GameStatus status,
            Long userId,
            User authenticatedUser,
            Pageable pageable
    );
}
