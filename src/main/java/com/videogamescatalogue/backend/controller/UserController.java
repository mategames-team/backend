package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.user.ChangePasswordRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UpdateUserRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserResponseDto;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseDto getUserInfo(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return userService.getUserInfo(id, user);
    }

    @PatchMapping("/me")
    public UserResponseDto updateUserInfo(
            @Valid @RequestBody UpdateUserRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return userService.updateUserInfo(requestDto, user);
    }

    @PatchMapping("/me/password")
    public UserResponseDto changePassword(
            @Valid @RequestBody ChangePasswordRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return userService.changePassword(requestDto, user);
    }
}
