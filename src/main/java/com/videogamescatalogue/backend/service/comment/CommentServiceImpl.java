package com.videogamescatalogue.backend.service.comment;

import com.videogamescatalogue.backend.dto.external.ApiResponseFullGameDto;
import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.exception.AccessNotAllowedException;
import com.videogamescatalogue.backend.mapper.comment.CommentMapper;
import com.videogamescatalogue.backend.mapper.game.GameMapper;
import com.videogamescatalogue.backend.model.Comment;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.repository.CommentRepository;
import com.videogamescatalogue.backend.repository.GameRepository;
import com.videogamescatalogue.backend.service.RawgApiClient;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final GameRepository gameRepository;
    private final RawgApiClient apiClient;
    private final GameMapper gameMapper;
    private final CommentRepository commentRepository;

    @Override
    public CommentDto create(Long gameApiId, CreateCommentRequestDto requestDto, User user) {
        Comment comment = commentMapper.toModel(requestDto);
        setGame(comment, gameApiId);
        comment.setUser(user);
        comment.setLocalDateTime(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public Page<CommentDto> getCommentsForGame(Long id, Pageable pageable) {
        Page<Comment> gameComments = commentRepository.findAllByGameApiId(id, pageable);
        return gameComments.map(commentMapper::toDto);
    }

    @Override
    public void delete(Long commentId, Long userId) {
        isCreatorOfComment(commentId, userId);
        commentRepository.deleteById(commentId);
    }

    private void isCreatorOfComment(Long commentId, Long userId) {
        if (!commentRepository.existsByIdAndUserId(commentId, userId)) {
            throw new AccessNotAllowedException("User with id: " + userId
                    + " is not allowed to delete comment with id: " + commentId);
        }
    }

    private void setGame(Comment comment, Long gameApiId) {
        Optional<Game> gameOptional = gameRepository.findByApiId(gameApiId);

        if (gameOptional.isEmpty()) {
            ApiResponseFullGameDto apiGame = apiClient.getGameById(gameApiId);
            Game game = gameMapper.toModel(apiGame);
            Game savedGame = gameRepository.save(game);
            comment.setGame(savedGame);
            comment.setGameApiId(savedGame.getApiId());
        } else {
            Game game = gameOptional.get();
            comment.setGame(game);
            comment.setGameApiId(game.getApiId());
        }
    }
}
