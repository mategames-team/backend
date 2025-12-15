package com.videogamescatalogue.backend.service.usergame;

import com.videogamescatalogue.backend.dto.internal.usergame.CreateUserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.model.UserGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserGameService {
    UserGameDto createOrUpdate(CreateUserGameDto createDto, User user);

    void delete(Long id, User user);

    Page<UserGameDto> getByStatus(
            UserGame.GameStatus status,
            Long userId,
            Pageable pageable
    );
}
