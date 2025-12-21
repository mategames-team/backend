package com.videogamescatalogue.backend.service.game;

import com.videogamescatalogue.backend.dto.external.ApiResponseFullGameDto;
import com.videogamescatalogue.backend.dto.external.ApiResponseGameDto;
import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.dto.internal.game.GameWithStatusDto;
import com.videogamescatalogue.backend.mapper.game.GameMapper;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.model.UserGame;
import com.videogamescatalogue.backend.repository.GameRepository;
import com.videogamescatalogue.backend.repository.SpecificationBuilder;
import com.videogamescatalogue.backend.repository.UserGameRepository;
import com.videogamescatalogue.backend.service.RawgApiClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final RawgApiClient apiClient;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;
    private final SpecificationBuilder<Game, GameSearchParameters> specificationBuilder;
    private final UserGameRepository userGameRepository;

    @Override
    public void fetchBestGames() {
        List<ApiResponseGameDto> apiGames = apiClient.getBestGames();
        log.info("Received list of games from Api. List size={}",
                apiGames.size());

        List<Game> modelList = gameMapper.toModelList(apiGames);

        Map<Long, Game> existingGamesMap = getExistingGamesMap(modelList);

        List<Game> toSaveGames = new ArrayList<>();
        for (Game game : modelList) {
            if (!existingGamesMap.containsKey(game.getApiId())) {
                toSaveGames.add(game);
            }
        }

        gameRepository.saveAll(toSaveGames);
        log.info("Saved {} games to DB", toSaveGames.size());
    }

    @Override
    public void fetchAndUpdateBestGames() {
        log.info("fetchAndUpdateBestGames is called");

        List<ApiResponseGameDto> apiGames = apiClient.getBestGames();
        log.info("Received list of games from Api. List size={}",
                apiGames.size());

        List<Game> modelList = gameMapper.toModelList(apiGames);

        Map<Long, Game> existingGamesMap = getExistingGamesMap(modelList);
        setIdIfExistingGame(modelList, existingGamesMap);

        gameRepository.saveAll(modelList);
        log.info("Saved and updated games");
    }

    @Override
    public Page<GameDto> getAllGamesFromDb(Pageable pageable) {
        return gameRepository.findAll(pageable)
                .map(gameMapper::toDto);
    }

    @Override
    public GameWithStatusDto getByApiId(Long apiId, User user) {
        Game game = findOrUpdate(apiId);

        UserGame.GameStatus status = getGameStatus(apiId, user);

        return gameMapper.toDtoWithStatus(game, status);
    }

    @Override
    public Page<GameDto> getAllGamesFromApi(Pageable pageable) {
        return apiClient.getAllGames(pageable)
                .map(gameMapper::toModel)
                .map(gameMapper::toDto);
    }

    @Override
    public Page<GameDto> search(GameSearchParameters searchParameters, Pageable pageable) {
        Specification<Game> specification = specificationBuilder.build(searchParameters);

        return gameRepository.findAll(specification, pageable)
                .map(gameMapper::toDto);
    }

    @Override
    public Page<GameDto> apiSearch(Map<String, String> searchParams) {
        Page<ApiResponseGameDto> apiGames = apiClient.search(searchParams);

        return apiGames.map(gameMapper::toModel)
                .map(gameMapper::toDto);
    }

    private Map<Long, Game> getExistingGamesMap(List<Game> modelList) {
        List<Long> apiIds = modelList.stream()
                .map(Game::getApiId)
                .toList();
        List<Game> existingGames = gameRepository.findAllByApiIdIn(apiIds);
        return existingGames.stream()
                .collect(Collectors.toMap(
                        Game::getApiId, Function.identity()
                ));
    }

    private void setIdIfExistingGame(List<Game> modelList, Map<Long, Game> existingGamesMap) {
        for (Game game : modelList) {
            if (existingGamesMap.containsKey(game.getApiId())) {
                game.setId(
                        existingGamesMap.get(game.getApiId()).getId()
                );
            }
        }
    }

    private UserGame.GameStatus getGameStatus(Long apiId, User user) {
        if (user == null) {
            return null;
        }

        Optional<UserGame> userGameOptional = userGameRepository.findByUserIdAndGameApiId(
                user.getId(), apiId
        );
        return userGameOptional.map(UserGame::getStatus)
                .orElse(null);
    }

    private Game findOrUpdate(Long apiId) {
        Optional<Game> gameOptional = gameRepository.findByApiId(apiId);
        if (gameOptional.isEmpty()) {
            return findFromApi(apiId);
        }
        Game game = gameOptional.get();
        if (game.getDescription() == null) {
            return updateGameDescription(apiId, game);
        }
        return game;
    }

    private Game updateGameDescription(Long apiId, Game game) {
        ApiResponseFullGameDto apiGame = apiClient.getGameById(apiId);
        game.setDescription(apiGame.description());
        return gameRepository.save(game);
    }

    private Game findFromApi(Long apiId) {
        ApiResponseFullGameDto apiGame = apiClient.getGameById(apiId);
        return gameMapper.toModel(apiGame);
    }
}
