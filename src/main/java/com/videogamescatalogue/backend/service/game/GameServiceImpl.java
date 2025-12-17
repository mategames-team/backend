package com.videogamescatalogue.backend.service.game;

import com.videogamescatalogue.backend.dto.external.ApiResponseFullGameDto;
import com.videogamescatalogue.backend.dto.external.ApiResponseGameDto;
import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.mapper.game.GameMapper;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.repository.GameRepository;
import com.videogamescatalogue.backend.repository.SpecificationBuilder;
import com.videogamescatalogue.backend.service.RawgApiClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final RawgApiClient apiClient;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;
    private final SpecificationBuilder<Game, GameSearchParameters> specificationBuilder;

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
        log.info("Saved games to DB");
    }

    @Override
    public void fetchAndUpdateBestGames() {
        List<ApiResponseGameDto> apiGames = apiClient.getBestGames();
        List<Game> modelList = gameMapper.toModelList(apiGames);

        Map<Long, Game> existingGamesMap = getExistingGamesMap(modelList);
        setIdIfExistingGame(modelList, existingGamesMap);

        gameRepository.saveAll(modelList);
    }

    @Override
    public Page<GameDto> getAllGamesFromDb(Pageable pageable) {
        return gameRepository.findAll(pageable)
                .map(gameMapper::toDto);
    }

    @Override
    public GameDto getByApiId(Long apiId) {
        Optional<Game> gameOptional = gameRepository.findByApiId(apiId);
        if (gameOptional.isEmpty()) {
            ApiResponseFullGameDto apiGame = apiClient.getGameById(apiId);
            Game game = gameMapper.toModel(apiGame);
            Game savedGame = gameRepository.save(game);
            return gameMapper.toDto(savedGame);
        }

        Game game = gameOptional.get();

        if (game.getDescription() == null) {
            ApiResponseFullGameDto apiGame = apiClient.getGameById(apiId);
            game.setDescription(apiGame.description());
            Game savedGame = gameRepository.save(game);
            return gameMapper.toDto(savedGame);
        }

        return gameMapper.toDto(game);
    }

    @Override
    public Page<GameDto> search(GameSearchParameters searchParameters, Pageable pageable) {
        Specification<Game> specification = specificationBuilder.build(searchParameters);

        return gameRepository.findAll(specification, pageable)
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
}
