package com.videogamescatalogue.backend.service.usergame;

import com.videogamescatalogue.backend.dto.external.ApiResponseFullGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.CreateUserGameDto;
import com.videogamescatalogue.backend.dto.internal.usergame.UserGameDto;
import com.videogamescatalogue.backend.exception.AccessNotAllowedException;
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
        //usergame is in db so update
        Optional<UserGame> userGameOptional = userGameRepository.findByUserIdAndGameApiId(
                user.getId(),
                createDto.apiId()
        );
        if (userGameOptional.isPresent()) {
            return updateUserGame(createDto.status(), userGameOptional.get());
        }

        ////create new usergame, check if game in db
        UserGame userGame = createNewUserGame(createDto, user);

        //////game in db
        Optional<Game> gameOptional = gameRepository.findByApiId(createDto.apiId());
        if (gameOptional.isPresent()) {
            return updateWithGameAndSave(userGame, gameOptional.get());
        }

        //////game not in db
        Game game = getGameFromApi(createDto.apiId());
        Game savedGame = gameRepository.save(game);

        return updateWithGameAndSave(userGame, savedGame);
    }

    @Override
    public void delete(Long id, User user) {
        checkBelongsToUser(id, user.getId());
        userGameRepository.deleteById(id);
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

    private UserGameDto updateWithGameAndSave(UserGame userGame, Game game) {
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

    private UserGameDto updateUserGame(UserGame.GameStatus status, UserGame userGame) {
        userGame.setStatus(status);
        UserGame savedUserGame = userGameRepository.save(userGame);
        return userGameMapper.toDto(savedUserGame);
    }

    private Game getGameFromApi(Long apiId) {
        ApiResponseFullGameDto apiGame = apiClient.getGameById(apiId);
        return gameMapper.toModel(apiGame);
    }
}
