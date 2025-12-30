package com.videogamescatalogue.backend.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.videogamescatalogue.backend.dto.internal.user.ChangePasswordRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UpdateUserRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserRegistrationRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserResponseDto;
import com.videogamescatalogue.backend.exception.InvalidInputException;
import com.videogamescatalogue.backend.exception.RegistrationException;
import com.videogamescatalogue.backend.mapper.user.UserMapper;
import com.videogamescatalogue.backend.model.Role;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    private String email = "test@gmail.com";
    private UserRegistrationRequestDto registrationRequestDto;
    private User user;
    private UserResponseDto responseDtoUser;
    private Role role;
    private UserResponseDto responseDtoBob;
    private User userBob;
    private UpdateUserRequestDto updateUserRequestDto;
    private ChangePasswordRequestDto changePasswordRequestDto;
    private ChangePasswordRequestDto changePasswordRequestDtoNoMatch;

    @BeforeEach
    void setUp() {
        registrationRequestDto = new UserRegistrationRequestDto(
                "profileName", "password5454764",
                "password5454764", "test@gmail.com",
                "about", "location"
        );
        role = new Role();
        role.setId(1L);
        role.setRole(Role.RoleName.ROLE_USER);

        user = new User();
        user.setId(10L);
        user.setProfileName("profileName");
        user.setPassword(
                "$2a$10$iHLvIfNAFWubZLSUjY4kCeB9.gMurj6ePKehoCcHnn0dWEZBvnY9i"
        );
        user.setEmail("test@gmail.com");
        user.setAbout("about");
        user.setLocation("location");
        user.getRoles().add(role);

        responseDtoUser = new UserResponseDto(
                10L, "profileName",
                "about", "location"
        );

        userBob = new User();
        userBob.setId(9L);
        userBob.setProfileName("profileNameBob");
        userBob.setPassword("hfgrhgthehethetheheheihd");
        userBob.setEmail("testBob@gmail.com");
        userBob.setAbout("aboutBob");
        userBob.setLocation("locationBob");
        userBob.getRoles().add(role);

        responseDtoBob = new UserResponseDto(
                9L, "profileNameBob",
                "aboutBob", "locationBob"
        );

        updateUserRequestDto = new UpdateUserRequestDto(
                "updated profileName", null,
                null, null
        );

        changePasswordRequestDto = new ChangePasswordRequestDto(
                "jfhgf747837",
                "newpassword36445",
                "newpassword36445"
        );
        changePasswordRequestDtoNoMatch = new ChangePasswordRequestDto(
                "password",
                "newpassword36445",
                "newpassword364"
        );
    }

    @Test
    void registerUser_userExists_throwException() {
        when(userRepository.existsByEmail(email))
                .thenReturn(true);
        assertThrows(RegistrationException.class,
                () -> userService.registerUser(
                        registrationRequestDto
                ));

    }

    @Test
    void registerUser_validRequest_returnUserDto() {
        String encodedPassword = user.getPassword();

        when(userRepository.existsByEmail(email))
                .thenReturn(false);
        when(userMapper.toModel(registrationRequestDto))
                .thenReturn(user);
        when(passwordEncoder.encode(
                registrationRequestDto.password()))
                .thenReturn(encodedPassword);
        when(userRepository.save(user))
                .thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDtoBob);

        UserResponseDto actual = userService.registerUser(
                registrationRequestDto
        );

        assertEquals(responseDtoBob, actual);

        verify(passwordEncoder).encode(registrationRequestDto.password());
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void getUserInfo_usersMatch_returnAuthenticatedUserInfo() {
        when(userMapper.toDto(user))
                .thenReturn(responseDtoBob);

        UserResponseDto actual = userService.getUserInfoById(10L, user);

        assertEquals(responseDtoBob, actual);

        verifyNoInteractions(userRepository);
    }

    @Test
    void getUserInfo_usersDoNotMatch_returnOtherUserInfo() {
        when(userRepository.findById(9L))
                .thenReturn(Optional.of(userBob));
        when(userMapper.toDto(userBob))
                .thenReturn(responseDtoBob);

        UserResponseDto actual = userService.getUserInfoById(9L, user);

        assertEquals(responseDtoBob, actual);
        assertNotEquals(responseDtoUser, actual);

        verify(userRepository).findById(9L);
        verify(userMapper).toDto(userBob);
    }

    @Test
    void updateUserInfo_validRequest_returnUserResponseDto() {
        when(userMapper.updateProfileInfo(
                user, updateUserRequestDto
        ))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDtoBob);

        UserResponseDto actual = userService.updateUserInfo(
                updateUserRequestDto, user
        );

        assertEquals(responseDtoBob, actual);

        verify(userRepository).save(user);
    }

    @Test
    void changePassword_currentPasswordInvalid_throwException() {
        when(passwordEncoder.matches(
                changePasswordRequestDto.currentPassword(),
                user.getPassword()
        )).thenReturn(false);
        assertThrows(InvalidInputException.class,
                () -> userService.changePassword(
                        changePasswordRequestDto, user
                ));

        verify(passwordEncoder).matches(
                changePasswordRequestDto.currentPassword(),
                user.getPassword()
        );
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoInteractions(userRepository, userMapper);
    }

    @Test
    void changePassword_passwordsDoNotMatch_throwException() {
        when(passwordEncoder.matches(
                changePasswordRequestDtoNoMatch.currentPassword(),
                user.getPassword()
        )).thenReturn(true);
        assertThrows(InvalidInputException.class,
                () -> userService.changePassword(
                        changePasswordRequestDtoNoMatch, user
                ));

        verify(passwordEncoder).matches(
                changePasswordRequestDtoNoMatch.currentPassword(),
                user.getPassword()
        );
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoInteractions(userRepository, userMapper);
    }

    @Test
    void changePassword_validRequest_returnUserResponseDto() {
        String oldPassword = user.getPassword();
        when(passwordEncoder.matches(
                changePasswordRequestDto.currentPassword(),
                oldPassword
        )).thenReturn(true);
        when(passwordEncoder.encode(
                changePasswordRequestDto.newPassword()
        ))
                .thenReturn("hfgahfghrgrlgnlrg/lng/lahdhtoi");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDtoBob);

        UserResponseDto actual = userService.changePassword(
                changePasswordRequestDto, user
        );

        assertNotNull(actual);
        assertEquals(responseDtoBob, actual);

        verify(passwordEncoder).matches(
                changePasswordRequestDto.currentPassword(),
                oldPassword
        );
        verify(passwordEncoder).encode(
                changePasswordRequestDto.newPassword()
        );
        verify(userRepository).save(user);
    }
}
