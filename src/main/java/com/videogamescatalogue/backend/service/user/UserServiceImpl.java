package com.videogamescatalogue.backend.service.user;

import com.videogamescatalogue.backend.dto.internal.user.ChangePasswordRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UpdateUserRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserRegistrationRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserResponseDto;
import com.videogamescatalogue.backend.exception.AccessNotAllowedException;
import com.videogamescatalogue.backend.exception.EntityNotFoundException;
import com.videogamescatalogue.backend.exception.InvalidInputException;
import com.videogamescatalogue.backend.exception.RegistrationException;
import com.videogamescatalogue.backend.mapper.user.UserMapper;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registerUser(UserRegistrationRequestDto requestDto) {
        checkUserAlreadyExists(requestDto.email());

        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));

        User savedUser = userRepository.save(user);

        log.info("User registered successfully, id={}",savedUser.getId());

        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto getUserInfo(Long userId, User authenticatedUser) {
        if (authenticatedUser.getId().equals(userId)) {
            return userMapper.toDto(authenticatedUser);
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "There is no user by id: " + userId
                )
        );
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUserInfo(UpdateUserRequestDto requestDto, User authenticatedUser) {
        User updatedUser = userMapper.updateProfileInfo(authenticatedUser, requestDto);
        User savedUser = userRepository.save(updatedUser);

        log.info("User with id={} updated profile info.", authenticatedUser.getId());

        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto changePassword(
            ChangePasswordRequestDto requestDto, User authenticatedUser
    ) {
        validateCurrentPassword(requestDto, authenticatedUser);
        checkPasswordsMatch(requestDto);

        authenticatedUser.setPassword(passwordEncoder.encode(requestDto.newPassword()));
        User savedUser = userRepository.save(authenticatedUser);

        log.info("User with id={} changed password.", authenticatedUser.getId());

        return userMapper.toDto(savedUser);
    }

    private void checkPasswordsMatch(ChangePasswordRequestDto requestDto) {
        if (!requestDto.newPassword().equals(requestDto.repeatPassword())) {
            throw new InvalidInputException("New password and repeat password must match");
        }
    }

    private void validateCurrentPassword(
            ChangePasswordRequestDto requestDto, User authenticatedUser
    ) {
        if (!passwordEncoder.matches(
                requestDto.currentPassword(),
                authenticatedUser.getPassword()
        )) {
            throw new InvalidInputException("Current password is not valid.");
        }
    }

    private void checkUserAlreadyExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RegistrationException("Can't register user. User with email: "
                    + email + " is already registered.");
        }
    }
}
