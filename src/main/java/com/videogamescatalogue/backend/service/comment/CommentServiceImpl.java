package com.videogamescatalogue.backend.service.comment;

import com.videogamescatalogue.backend.dto.external.ApiResponseFullGameDto;
import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.mapper.comment.CommentMapper;
import com.videogamescatalogue.backend.mapper.game.GameMapper;
import com.videogamescatalogue.backend.model.Comment;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.repository.GameRepository;
import com.videogamescatalogue.backend.service.RawgApiClient;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final GameRepository gameRepository;
    private final RawgApiClient apiClient;
    private final GameMapper gameMapper;

    @Override
    public CommentDto create(Long gameApiId, CreateCommentRequestDto requestDto, User user) {
        Comment comment = commentMapper.toModel(requestDto);
        setGame(comment, gameApiId);
        comment.setUser(user);
        return commentMapper.toDto(comment);
    }

    private void setGame(Comment comment, Long gameApiId) {
        Optional<Game> gameOptional = gameRepository.findByApiId(gameApiId);

        if (gameOptional.isEmpty()) {
            ApiResponseFullGameDto apiGame = apiClient.getGameById(gameApiId);
            Game game = gameMapper.toModel(apiGame);
            Game savedGame = gameRepository.save(game);
            comment.setGame(savedGame);
        } else {
            Game game = gameOptional.get();
            comment.setGame(game);
        }
    }
}
