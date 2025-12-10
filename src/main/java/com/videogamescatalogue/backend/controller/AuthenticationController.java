package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.user.UserLoginRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserLoginResponseDto;
import com.videogamescatalogue.backend.dto.internal.user.UserRegistrationRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserResponseDto;
import com.videogamescatalogue.backend.security.AuthenticationService;
import com.videogamescatalogue.backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    UserResponseDto registerUser(
            @RequestBody @Valid UserRegistrationRequestDto requestDto
    ) {
        return userService.registerUser(requestDto);
    }

    @PostMapping("/login")
    UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
