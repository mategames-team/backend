package com.videogamescatalogue.backend.service.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.videogamescatalogue.backend.dto.external.ApiPlatformWrapper;
import com.videogamescatalogue.backend.dto.external.ApiResponseDeveloperDto;
import com.videogamescatalogue.backend.dto.external.ApiResponseFullGameDto;
import com.videogamescatalogue.backend.dto.external.ApiResponseGameDto;
import com.videogamescatalogue.backend.dto.external.ApiResponseGenreDto;
import com.videogamescatalogue.backend.dto.external.ApiResponsePlatformDto;
import com.videogamescatalogue.backend.dto.internal.developer.DeveloperDto;
import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.dto.internal.game.GameWithStatusDto;
import com.videogamescatalogue.backend.dto.internal.genre.GenreDto;
import com.videogamescatalogue.backend.dto.internal.platform.PlatformDto;
import com.videogamescatalogue.backend.mapper.game.GameMapper;
import com.videogamescatalogue.backend.model.Developer;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.Genre;
import com.videogamescatalogue.backend.model.Platform;
import com.videogamescatalogue.backend.repository.GameRepository;
import com.videogamescatalogue.backend.service.RawgApiClient;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {
    @Mock
    private RawgApiClient apiClient;
    @Mock
    private GameMapper gameMapper;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private GameServiceImpl gameService;
    private List<ApiResponseGameDto> apiResponse;
    private ApiResponseGameDto apiResponseGameDto;
    private List<Game> gameModelList;
    private List<Long> responseGamesIds;
    private List<Game> zeroGamesToSave;
    private Pageable pageable;
    private List<Game> oneGameToSave;
    private Page<Game> oneGamePage;
    private Game gameModel;
    private GameDto gameDto;
    private Page<GameDto> oneGameDtoPage;
    private GameWithStatusDto gameWithDescrAndNullStatusDto;
    private ApiResponseFullGameDto apiResponseFullGameDto;
    private Game gameModelWithDescription;

    @BeforeEach
    void setUp() {
        ApiPlatformWrapper apiPlatformWrapper = new ApiPlatformWrapper(
                new ApiResponsePlatformDto("PC")
        );
        ApiResponseGenreDto apiResponseGenreDto = new ApiResponseGenreDto("Action");
        apiResponseGameDto = new ApiResponseGameDto(
                1L, "Game",
                "2025-12-12", "link",
                List.of(apiPlatformWrapper),
                List.of(apiResponseGenreDto),
                BigDecimal.valueOf(4.75));
        apiResponse = List.of(apiResponseGameDto);

        Platform platformModel = new Platform();
        platformModel.setId(10L);
        platformModel.setGeneralName(Platform.GeneralName.PC);
        Genre genreModel = new Genre();
        genreModel.setId(20L);
        genreModel.setName(Genre.Name.ACTION);

        Developer developer = new Developer();
        developer.setId(30L);
        developer.setApiId(457389L);
        developer.setName("Developer");

        gameModel = new Game();
        gameModel.setApiId(1L);
        gameModel.setName("Game");
        gameModel.setYear(2025);
        gameModel.setBackgroundImage("link");
        gameModel.setPlatforms(Set.of(platformModel));
        gameModel.setGenres(Set.of(genreModel));
        gameModel.setApiRating(BigDecimal.valueOf(4.75));
        gameModel.setDescription(null);
        gameModel.setDevelopers(Set.of(developer));

        gameModelList = List.of(gameModel);
        responseGamesIds = List.of(1L);
        zeroGamesToSave = List.of();
        pageable = PageRequest.of(0, 30);
        oneGameToSave = List.of(gameModel);
        oneGamePage = new PageImpl<>(gameModelList);

        PlatformDto platformDto = new PlatformDto("PC");
        GenreDto genreDto = new GenreDto("Action");
        DeveloperDto developerDto = new DeveloperDto(
                30L, 457389L, "Developer"
        );
        gameDto = new GameDto(
                1L, "Game", 2025, "link",
                Set.of(platformDto), Set.of(genreDto),
                Set.of(developerDto),
                BigDecimal.valueOf(4.75), null
        );
        oneGameDtoPage = new PageImpl<>(List.of(gameDto));
        gameWithDescrAndNullStatusDto = new GameWithStatusDto(
                1L, "Game", 2025, "link",
                Set.of(platformDto), Set.of(genreDto),
                Set.of(developerDto),
                BigDecimal.valueOf(4.75),
                "description", null
        );

        ApiResponseDeveloperDto apiResponseDeveloperDto =
                new ApiResponseDeveloperDto(
                        457389L, "Developer"
                );

        apiResponseFullGameDto = new ApiResponseFullGameDto(
                1L, "Game", "description",
                "2025-12-12", "link",
                List.of(apiPlatformWrapper),
                List.of(apiResponseGenreDto),
                List.of(apiResponseDeveloperDto),
                BigDecimal.valueOf(4.75)
        );

        gameModelWithDescription = new Game();
        gameModelWithDescription.setApiId(1L);
        gameModelWithDescription.setName("Game");
        gameModelWithDescription.setYear(2025);
        gameModelWithDescription.setBackgroundImage("link");
        gameModelWithDescription.setPlatforms(Set.of(platformModel));
        gameModelWithDescription.setGenres(Set.of(genreModel));
        gameModelWithDescription.setApiRating(BigDecimal.valueOf(4.75));
        gameModelWithDescription.setDescription("description");
    }

    @Test
    void fetchBestGames_NoNewGames_SavesZeroGames() {
        when(apiClient.getBestGames())
                .thenReturn(apiResponse);
        when(gameMapper.toModelList(apiResponse))
                .thenReturn(gameModelList);
        when(gameRepository.findAllByApiIdIn(responseGamesIds))
                .thenReturn(gameModelList);
        when(gameRepository.saveAll(zeroGamesToSave))
                .thenReturn(null);

        gameService.fetchBestGames();

        verify(apiClient).getBestGames();
        verify(gameMapper).toModelList(apiResponse);
        verify(gameRepository).findAllByApiIdIn(responseGamesIds);
        verify(gameRepository).saveAll(zeroGamesToSave);
        verifyNoMoreInteractions(
                apiClient, gameMapper, gameRepository
        );
    }

    @Test
    void fetchAndUpdateBestGames_validRequest_SavesAndUpdates() {
        when(apiClient.getBestGames())
                .thenReturn(apiResponse);
        when(gameMapper.toModelList(apiResponse))
                .thenReturn(gameModelList);
        when(gameRepository.findAllByApiIdIn(responseGamesIds))
                .thenReturn(gameModelList);
        when(gameRepository.saveAll(oneGameToSave))
                .thenReturn(null);

        gameService.fetchAndUpdateBestGames();

        verify(apiClient).getBestGames();
        verify(gameMapper).toModelList(apiResponse);
        verify(gameRepository).findAllByApiIdIn(responseGamesIds);
        verify(gameRepository).saveAll(oneGameToSave);
        verifyNoMoreInteractions(apiClient, gameMapper, gameRepository);
    }

    @Test
    void getAllGamesFromDb_validRequest_returnsPageGameDtos() {
        when(gameRepository.findAll(pageable))
                .thenReturn(oneGamePage);
        when(gameMapper.toDto(gameModel)).thenReturn(gameDto);

        Page<GameDto> actual = gameService.getAllGamesFromDb(pageable);

        assertNotNull(actual);
        assertEquals(oneGameDtoPage, actual);

        verify(gameRepository).findAll(pageable);
        verify(gameMapper).toDto(gameModel);
        verifyNoMoreInteractions(gameMapper, gameRepository);
    }

    @Test
    void getByApiId_gameWithDescrInDbNoLoggedUser_returnsGameWithStatusDto() {
        Long apiId = 1L;
        gameModel.setDescription("description");

        when(gameRepository.findByApiId(apiId))
                .thenReturn(Optional.of(gameModel));
        when(gameMapper.toDtoWithStatus(gameModel, null))
                .thenReturn(gameWithDescrAndNullStatusDto);

        GameWithStatusDto actual = gameService.getByApiId(apiId, null);

        assertNotNull(actual);
        assertEquals(gameWithDescrAndNullStatusDto, actual);
        assertNull(actual.status());

        verify(gameRepository).findByApiId(apiId);
        verify(gameMapper).toDtoWithStatus(gameModel, null);
        verifyNoMoreInteractions(gameMapper, gameRepository);
        verifyNoInteractions(apiClient);
    }

    @Test
    void getByApiId_gameWithoutDescrInDbNoLoggedUser_returnsGameWithStatusDto() {
        Long apiId = 1L;

        when(gameRepository.findByApiId(apiId))
                .thenReturn(Optional.of(gameModel));
        when(apiClient.getGameById(apiId))
                .thenReturn(apiResponseFullGameDto);
        when(gameRepository.save(gameModel))
                .thenReturn(gameModel);

        when(gameMapper.toDtoWithStatus(gameModel, null))
                .thenReturn(gameWithDescrAndNullStatusDto);

        GameWithStatusDto actual = gameService.getByApiId(apiId, null);

        assertNotNull(actual);
        assertEquals(gameWithDescrAndNullStatusDto, actual);
        assertNull(actual.status());

        verify(gameRepository).findByApiId(apiId);
        verify(gameMapper).toDtoWithStatus(gameModel, null);
        verify(apiClient).getGameById(apiId);
        verifyNoMoreInteractions(gameMapper, gameRepository, apiClient);
    }

    @Test
    void getAllGamesFromApi_validRequest_returnPageGameDto() {
        when(apiClient.getAllGames(pageable))
                .thenReturn(new PageImpl<>(apiResponse));
        when(gameMapper.toModel(apiResponseGameDto))
                .thenReturn(gameModel);
        when(gameMapper.toDto(gameModel)).thenReturn(gameDto);

        Page<GameDto> actual = gameService.getAllGamesFromApi(pageable);

        assertNotNull(actual);
        assertEquals(new PageImpl<>(
                List.of(gameDto)), actual
        );

        verify(apiClient).getAllGames(pageable);
        verifyNoInteractions(gameRepository);
    }

    @Test
    void search_validRequest_returnPageGameDto() {
        verifyNoInteractions(apiClient);
    }

    @Test
    void apiSearch() {
        verifyNoInteractions(gameRepository);
    }
}
