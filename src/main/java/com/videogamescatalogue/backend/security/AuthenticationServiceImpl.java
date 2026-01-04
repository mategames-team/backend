package com.videogamescatalogue.backend.security;

import com.videogamescatalogue.backend.dto.internal.user.UserLoginRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserLoginResponseDto;
import com.videogamescatalogue.backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.email(), requestDto.password())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        return new UserLoginResponseDto(token, userId);
    }
}
