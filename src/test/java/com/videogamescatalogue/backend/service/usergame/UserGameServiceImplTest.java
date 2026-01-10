package com.videogamescatalogue.backend.service.usergame;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.dto.internal.genre.GenreDto;
import com.videogamescatalogue.backend.dto.internal.platform.PlatformDto;
import com.videogamescatalogue.backend.dto.internal.usergame.CreateUserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.mapper.usergame.UserGameMapper;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.Role;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.model.UserGame;
import com.videogamescatalogue.backend.repository.GameRepository;
import com.videogamescatalogue.backend.repository.UserGameRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserGameServiceImplTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserGameRepository userGameRepository;
    @Mock
    private UserGameMapper userGameMapper;
    @InjectMocks
    private UserGameServiceImpl userGameService;
    private CreateUserGameDto createUserGameDto;
    private Role role;
    private User user;
    private UserGame userGame;
    private UserGameDto userGameDto;
    private GameDto gameDto;
    private PlatformDto platformDto;
    private GenreDto genreDto;

    @BeforeEach
    void setUp() {
        createUserGameDto = new CreateUserGameDto(
                1L, UserGame.GameStatus.BACKLOG
        );

        role = new Role();
        role.setId(1L);
        role.setRole(Role.RoleName.ROLE_USER);

        user = new User();
        user.setId(10L);
        user.setProfileName("profileName");
        user.setPassword("$2a$10$iHLvIfNAFWubZLSUjY4kCeB9.gMurj6ePKehoCcHnn0dWEZBvnY9i");
        user.setEmail("test@gmail.com");
        user.setAbout("about");
        user.setLocation("location");
        user.getRoles().add(role);

        Game game = new Game();
        game.setApiId(100L);
        userGame = new UserGame();
        userGame.setUser(user);
        userGame.setGame(game);
        userGame.setStatus(UserGame.GameStatus.BACKLOG);

        platformDto = new PlatformDto("PC");

        genreDto = new GenreDto("Action");

        gameDto = new GameDto(
               1234L, "Game name",
                2016, "link",
                Set.of(platformDto),
                Set.of(genreDto),
                BigDecimal.valueOf(4.8),
                "description"
        );

        userGameDto = new UserGameDto(
                1L, 10L, gameDto,
                UserGame.GameStatus.BACKLOG.getValue()
        );
    }

    @Test
    void createOrUpdate_alreadyExists_update() {
        when(userGameRepository.findByUser_IdAndGame_ApiId(
                user.getId(),
                createUserGameDto.apiId()
        )).thenReturn(Optional.of(userGame));
        when(userGameRepository.save(userGame))
                .thenReturn(userGame);
        when(userGameMapper.toDto(userGame))
                .thenReturn(userGameDto);

        UserGameDto actual = userGameService.createOrUpdate(
                createUserGameDto, user
        );

        assertNotNull(actual);
        assertEquals(userGameDto, actual);

        verify(userGameRepository).findByUser_IdAndGame_ApiId(
                user.getId(),
                createUserGameDto.apiId()
        );
        verifyNoInteractions(gameRepository);
    }

    @Test
    void createOrUpdate_newUserGameAndGameInDb_createUserGameDto() {
        when(userGameRepository.findByUser_IdAndGame_ApiId(
                user.getId(),
                createUserGameDto.apiId()
        )).thenReturn(Optional.empty());
        when(gameRepository.findByApiId(createUserGameDto.apiId()))
                .thenReturn(Optional.of(new Game()));
        when(userGameRepository.save(
                any(UserGame.class)
        ))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
        when(userGameMapper.toDto(
                any(UserGame.class))
        ).thenReturn(userGameDto);

        UserGameDto actual = userGameService.createOrUpdate(
                createUserGameDto, user
        );

        assertNotNull(actual);
        assertEquals(userGameDto, actual);

        verify(userGameRepository).findByUser_IdAndGame_ApiId(
                user.getId(),
                createUserGameDto.apiId()
        );
        verify(gameRepository).findByApiId(createUserGameDto.apiId());
    }

    @Test
    void delete_validRequest_delete() {
        Long userGameId = userGame.getId();
        when(userGameRepository.findByUser_IdAndGame_ApiId(
                user.getId(), userGame.getGame().getApiId()
        ))
                .thenReturn(Optional.of(userGame));

        assertDoesNotThrow(() -> userGameService.delete(
                userGame.getGame().getApiId(), user
        ));

        verify(userGameRepository).findByUser_IdAndGame_ApiId(
                user.getId(), userGame.getGame().getApiId()
        );
        verify(userGameRepository).delete(userGame);
    }
}
