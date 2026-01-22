package com.videogamescatalogue.backend.service.user;

import com.videogamescatalogue.backend.dto.internal.user.ChangePasswordRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UpdateUserRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserRegistrationRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserRegistrationResponseDto;
import com.videogamescatalogue.backend.dto.internal.user.UserResponseDto;
import com.videogamescatalogue.backend.model.User;

public interface UserService {
    UserRegistrationResponseDto registerUser(
            UserRegistrationRequestDto requestDto
    );

    UserResponseDto getUserInfo(Long userId, User authenticatedUser);

    UserResponseDto updateUserInfo(UpdateUserRequestDto requestDto, User authenticatedUser);

    UserResponseDto changePassword(ChangePasswordRequestDto requestDto, User authenticatedUser);
}
