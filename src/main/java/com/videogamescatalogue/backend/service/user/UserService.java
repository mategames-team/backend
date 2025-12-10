package com.videogamescatalogue.backend.service.user;

import com.videogamescatalogue.backend.dto.internal.user.UserRegistrationRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(
            UserRegistrationRequestDto requestDto
    );
}
