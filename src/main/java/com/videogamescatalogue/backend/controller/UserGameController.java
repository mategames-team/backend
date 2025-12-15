package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.usergame.CreateUserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.model.UserGame;
import com.videogamescatalogue.backend.service.usergame.UserGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user-games")
@RestController
public class UserGameController {
    public static final int DEFAULT_PAGE_SIZE = 30;
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

    @GetMapping
    public Page<UserGameDto> getByStatus(
            @RequestParam UserGame.GameStatus status,
            @AuthenticationPrincipal User user,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        return userGameService.getByStatus(
                status, user.getId(), pageable
        );
    }
}
