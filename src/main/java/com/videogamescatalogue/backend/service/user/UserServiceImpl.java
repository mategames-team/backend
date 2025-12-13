package com.videogamescatalogue.backend.service.user;

import com.videogamescatalogue.backend.dto.internal.user.UserRegistrationRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserResponseDto;
import com.videogamescatalogue.backend.exception.RegistrationException;
import com.videogamescatalogue.backend.mapper.user.UserMapper;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return userMapper.toDto(savedUser);
    }

    private void checkUserAlreadyExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RegistrationException("Can't register user. User with email: "
                    + email + " is already registered.");
        }
    }
}
