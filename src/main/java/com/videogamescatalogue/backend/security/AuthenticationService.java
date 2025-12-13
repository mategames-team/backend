package com.videogamescatalogue.backend.security;

import com.videogamescatalogue.backend.dto.internal.user.UserLoginRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto requestDto);
}
