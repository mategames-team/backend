package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.usergame.CreateUserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.service.usergame.UserGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user-games")
@RestController
public class UserGameController {
    private final UserGameService userGameService;

    @PostMapping
    public UserGameDto createOrUpdate(
            @RequestBody CreateUserGameDto createDto,
            @AuthenticationPrincipal User user
    ) {
        return userGameService.createOrUpdate(createDto, user);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        userGameService.delete(id, user);
    }
}
