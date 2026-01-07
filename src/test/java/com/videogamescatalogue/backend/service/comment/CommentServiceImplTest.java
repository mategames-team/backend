package com.videogamescatalogue.backend.service.comment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.dto.internal.comment.UpdateCommentRequestDto;
import com.videogamescatalogue.backend.exception.AccessNotAllowedException;
import com.videogamescatalogue.backend.mapper.comment.CommentMapper;
import com.videogamescatalogue.backend.model.Comment;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.model.Platform;
import com.videogamescatalogue.backend.model.Role;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.repository.CommentRepository;
import com.videogamescatalogue.backend.repository.GameRepository;
import com.videogamescatalogue.backend.service.RawgApiClient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private RawgApiClient apiClient;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentServiceImpl commentService;
    private Game game;
    private CreateCommentRequestDto createCommentRequestDto;
    private Role role;
    private User user;
    private Comment comment;
    private CommentDto commentDto;
    private Pageable pageable;
    private Page<Comment> commentPage;
    private Page<CommentDto> commentDtoPage;
    private UpdateCommentRequestDto updateCommentRequestDto;
    private CommentDto commentDtoUpdated;

    @BeforeEach
    void setUp() {
        Platform platformModel = new Platform();
        platformModel.setId(10L);
        platformModel.setGeneralName(Platform.GeneralName.PC);
        Genre genreModel = new Genre();
        genreModel.setId(20L);
        genreModel.setName(Genre.Name.ACTION);
        game = new Game();
        game.setApiId(1L);
        game.setName("Game");
        game.setYear(2025);
        game.setBackgroundImage("link");
        game.setPlatforms(Set.of(platformModel));
        game.setGenres(Set.of(genreModel));
        game.setApiRating(BigDecimal.valueOf(4.75));
        game.setDescription("description");

        createCommentRequestDto = new CreateCommentRequestDto(
                "comment text", 5
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

        comment = new Comment();
        comment.setText("comment text");
        comment.setRating(5);
        comment.setUser(user);

        commentDto = new CommentDto(
                1L, 1L, "GameName", 10L,
                "user", "comment text",
                LocalDateTime.now(), 5
        );

        pageable = PageRequest.of(0, 30);

        commentPage = new PageImpl<>(List.of(comment));

        commentDtoPage = new PageImpl<>(List.of(commentDto));

        updateCommentRequestDto = new UpdateCommentRequestDto(
                "comment text updated", null
        );

        commentDtoUpdated = new CommentDto(
                2L, 1L, "GameName", 10L,
                "user", "comment text updated",
                LocalDateTime.now(), 5
        );
    }

    @Test
    void create_validRequestAndGameInDb_returnCommentDto() {
        Long apiId = game.getApiId();
        when(commentMapper.toModel(createCommentRequestDto))
                .thenReturn(comment);
        when(gameRepository.findByApiId(apiId)).thenReturn(Optional.of(game));
        when(commentRepository.save(any())).thenAnswer(
                invocation -> invocation.getArguments()[0]);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto actual = commentService.create(
                apiId, createCommentRequestDto, user
        );

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());
        Comment captured = captor.getValue();

        assertNotNull(actual);
        assertEquals(commentDto, actual);
        assertNotNull(captured.getGame());
        assertNotNull(captured.getUser());
        assertNotNull(captured.getLocalDateTime());

        verify(gameRepository).findByApiId(apiId);
        verifyNoInteractions(apiClient);
    }

    @Test
    void getCommentsForGame_validRequest_returnPageCommentDto() {
        Long apiId = game.getApiId();

        when(commentRepository.findAllByGameApiId(apiId, pageable))
                .thenReturn(commentPage);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        Page<CommentDto> actual = commentService.getCommentsForGame(apiId, pageable);

        assertNotNull(actual);
        assertEquals(commentDtoPage, actual);
    }

    @Test
    void getUserComments_validRequest_returnPageCommentDto() {
        Long userId = user.getId();

        when(commentRepository.findAllByUserId(userId, pageable))
                .thenReturn(commentPage);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        Page<CommentDto> actual = commentService.getUserComments(user, userId, pageable);

        assertNotNull(actual);
        assertEquals(commentDtoPage, actual);
    }

    @Test
    void update_userIsCreator_update() {
        Long commentId = comment.getId();
        Long userId = user.getId();
        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));
        when(commentRepository.save(comment))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
        when(commentMapper.toDto(any()))
                .thenReturn(commentDtoUpdated);

        CommentDto actual = commentService.update(
                commentId, updateCommentRequestDto, userId
        );

        assertDoesNotThrow(() -> commentService.update(
                commentId, updateCommentRequestDto, userId
        ));
        assertNotNull(actual);
        assertEquals(commentDtoUpdated, actual);
    }

    @Test
    void update_userIsNotCreator_throwException() {
        Long commentId = comment.getId();
        Long userId = 100L;
        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        assertThrows(AccessNotAllowedException.class,
                () -> commentService.update(
                        commentId, updateCommentRequestDto, userId
                ));

        verify(commentRepository).findById(commentId);
        verifyNoMoreInteractions(commentRepository);
        verifyNoInteractions(commentMapper);
    }

    @Test
    void delete_userIsCreator_delete() {
        Long commentId = comment.getId();
        Long userId = user.getId();

        when(commentRepository.existsByIdAndUserId(commentId, userId))
                .thenReturn(true);
        doNothing().when(commentRepository).deleteById(commentId);

        assertDoesNotThrow(() -> commentService.delete(commentId, userId));
    }

    @Test
    void delete_userIsNotCreator_throwException() {
        Long commentId = comment.getId();
        Long userId = user.getId();

        when(commentRepository.existsByIdAndUserId(commentId, userId))
                .thenReturn(false);

        assertThrows(
                AccessNotAllowedException.class,
                () -> commentService.delete(commentId, userId)
        );
    }
}
