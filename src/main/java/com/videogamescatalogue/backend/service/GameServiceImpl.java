package com.videogamescatalogue.backend.service;

import com.videogamescatalogue.backend.dto.external.ApiResponseGameDto;
import com.videogamescatalogue.backend.dto.internal.GameDto;
import com.videogamescatalogue.backend.dto.internal.GameSearchParameters;
import com.videogamescatalogue.backend.exception.EntityNotFoundException;
import com.videogamescatalogue.backend.mapper.GameMapper;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.model.Platform;
import com.videogamescatalogue.backend.repository.GameRepository;
import com.videogamescatalogue.backend.repository.SpecificationBuilder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final RawgApiClient apiClient;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;
    private final SpecificationBuilder<Game, GameSearchParameters> specificationBuilder;

    @Override
    public void fetchFromDb() {
        List<ApiResponseGameDto> apiGames = apiClient.getAllGames();

        List<Game> modelList = gameMapper.toModelList(apiGames);

        gameRepository.saveAll(modelList);
    }

    @Override
    public Page<GameDto> getAllGamesFromDb(Pageable pageable) {
        return gameRepository.findAll(pageable)
                .map(gameMapper::toDto);
    }

    @Override
    public GameDto getFromDbByApiId(Long apiId) {
        Optional<Game> gameOptional = gameRepository.findByApiId(apiId);
        if (gameOptional.isEmpty()) {
            throw new EntityNotFoundException("There is no game in DB by api id:" + apiId);
        }
        return gameMapper.toDto(gameOptional.get());
    }

    @Override
    public Page<GameDto> search(GameSearchParameters searchParameters, Pageable pageable) {
        Specification<Game> specification = specificationBuilder.build(searchParameters);

        return gameRepository.findAll(specification, pageable)
                .map(gameMapper::toDto);
    }
}
