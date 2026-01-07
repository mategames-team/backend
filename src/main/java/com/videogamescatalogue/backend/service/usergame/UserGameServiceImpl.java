package com.videogamescatalogue.backend.service.usergame;

import com.videogamescatalogue.backend.dto.external.ApiResponseFullGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.CreateUserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.exception.AccessNotAllowedException;
import com.videogamescatalogue.backend.exception.AuthenticationRequiredException;
import com.videogamescatalogue.backend.exception.EntityNotFoundException;
import com.videogamescatalogue.backend.mapper.game.GameMapper;
import com.videogamescatalogue.backend.mapper.usergame.UserGameMapper;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.model.UserGame;
import com.videogamescatalogue.backend.repository.GameRepository;
import com.videogamescatalogue.backend.repository.UserGameRepository;
import com.videogamescatalogue.backend.service.RawgApiClient;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserGameServiceImpl implements UserGameService {
    private final GameRepository gameRepository;
    private final UserGameRepository userGameRepository;
    private final RawgApiClient apiClient;
    private final UserGameMapper userGameMapper;
    private final GameMapper gameMapper;

    @Override
    public UserGameDto createOrUpdate(CreateUserGameDto createDto, User user) {
        Optional<UserGame> userGameOptional = userGameRepository.findByUserIdAndGameApiId(
                user.getId(),
                createDto.apiId()
        );
        if (userGameOptional.isPresent()) {
            return updateUserGameStatus(createDto.status(), userGameOptional.get());
        }

        UserGame userGame = createNewUserGame(createDto, user);

        Optional<Game> gameOptional = gameRepository.findByApiId(createDto.apiId());
        if (gameOptional.isPresent()) {
            return addGameAndSave(userGame, gameOptional.get());
        }

        Game game = getGameFromApi(createDto.apiId());
        Game savedGame = gameRepository.save(game);

        return addGameAndSave(userGame, savedGame);
    }

    @Override
    public void delete(Long id, User user) {
        checkBelongsToUser(id, user.getId());
        userGameRepository.deleteById(id);
    }

    @Override
    public Page<UserGameDto> getByStatus(
            UserGame.GameStatus status,
            Long userId,
            User authenticatedUser,
            Pageable pageable) {
        if (authenticatedUser == null && userId == null) {
            throw new AuthenticationRequiredException("Authentication is required");
        }
        if (userId == null) {
            return findByUserIdAndStatus(
                    authenticatedUser.getId(), status, pageable
            );
        }
        return findByUserIdAndStatus(userId,status,pageable);
    }

    private Page<UserGameDto> findByUserIdAndStatus(Long userId, UserGame.GameStatus status, Pageable pageable) {
        Page<UserGame> userGames = userGameRepository.findByUserIdAndStatus(
                userId, status, pageable
        );
        return userGames.map(userGameMapper::toDto);
    }

    private void checkBelongsToUser(Long id, Long userId) {
        Optional<UserGame> userGameOptional = userGameRepository.findById(id);
        if (userGameOptional.isEmpty()) {
            throw new EntityNotFoundException("There is no userGame by id: " + id);
        }

        Long userGameUserId = userGameOptional.get().getUser().getId();
        if (!userGameUserId.equals(userId)) {
            throw new AccessNotAllowedException("User with id: "
                    + userId + "is not allowed to access userGame with id: " + id);
        }
    }

    private UserGameDto addGameAndSave(UserGame userGame, Game game) {
        userGame.setGame(game);
        UserGame savedUserGame = userGameRepository.save(userGame);
        return userGameMapper.toDto(savedUserGame);
    }

    private UserGame createNewUserGame(CreateUserGameDto createDto, User user) {
        UserGame userGame = new UserGame();
        userGame.setUser(user);
        userGame.setStatus(createDto.status());
        return userGame;
    }

    private UserGameDto updateUserGameStatus(UserGame.GameStatus status, UserGame userGame) {
        userGame.setStatus(status);
        UserGame savedUserGame = userGameRepository.save(userGame);
        return userGameMapper.toDto(savedUserGame);
    }

    private Game getGameFromApi(Long apiId) {
        ApiResponseFullGameDto apiGame = apiClient.getGameById(apiId);
        return gameMapper.toModel(apiGame);
    }
}
