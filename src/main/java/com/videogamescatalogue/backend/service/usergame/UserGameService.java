package com.videogamescatalogue.backend.service.usergame;

import com.videogamescatalogue.backend.dto.internal.usergame.CreateUserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.model.User;

public interface UserGameService {
    UserGameDto createOrUpdate(CreateUserGameDto createDto, User user);

    void delete(Long id, User user);

}
