package com.videogamescatalogue.backend.service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.videogamescatalogue.backend.dto.external.ApiResponseFullGameDto;
import com.videogamescatalogue.backend.dto.external.ApiResponseGameDto;
import com.videogamescatalogue.backend.dto.external.ApiResponseGames;
import com.videogamescatalogue.backend.exception.ApiException;
import com.videogamescatalogue.backend.exception.HttpResponseException;
import com.videogamescatalogue.backend.exception.ObjectMapperException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class RawgApiClient {
    private static final String BASE_URL = "https://api.rawg.io/api/";
    private static final String GAME_URL_PART = "games";
    private static final String KEY_URL_PART = "?key=";
    private static final String PAGE_NUMBER_URL_PART = "&page=";
    private static final String PAGE_SIZE_URL_PART = "&page_size=";
    private static final String DEFAULT_PAGE_SIZE = "30";
    private static final String ORDERING_URL_PART = "&ordering=";
    private static final String ORDERING_ADDED_DESC = "-added";
    private static final String EXCLUDE_ADDITIONS_URL_PART = "&exclude_additions=true";
    private static final String DATES_BETWEEN_URL_PART = "&dates=" + LocalDate.now()
            + "%2C" + LocalDate.now().minusMonths(12);
    private static final String METACRITIC_URL_PART = "metacritic=80%2C100";

    @Value("${rawg.key}")
    private String apiKey;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public List<ApiResponseGameDto> getBestGames() {
        ArrayList<ApiResponseGameDto> result = new ArrayList<>();

        for (int i = 1; i < 11; i++) {
            log.info("Create request for page {}", i);

            String url = BASE_URL + GAME_URL_PART
                    + KEY_URL_PART + apiKey
                    + ORDERING_URL_PART + ORDERING_ADDED_DESC
                    + EXCLUDE_ADDITIONS_URL_PART
                    + DATES_BETWEEN_URL_PART
                    + METACRITIC_URL_PART
                    + PAGE_SIZE_URL_PART + DEFAULT_PAGE_SIZE
                    + PAGE_NUMBER_URL_PART + i;
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .header("User-Agent", "VideoGamesCatalogue")
                    .build();
            ApiResponseGames responseObject = getResponseGamesList(httpRequest);

            result.addAll(responseObject.results());

            log.info("Added games to resul list. Result list size={}",
                    result.size());
        }

        log.info("Formed result list with fetched games to return. List size={}",
                result.size()
        );

        return result;
    }

    public Page<ApiResponseGameDto> getAllGames(Pageable pageable) {
        String url = BASE_URL + GAME_URL_PART
                + KEY_URL_PART + apiKey
                + PAGE_SIZE_URL_PART + pageable.getPageSize()
                + PAGE_NUMBER_URL_PART + pageable.getPageNumber();

        String ordering = toRawgOrdering(pageable.getSort());
        if (ordering != null) {
            url = url + ORDERING_URL_PART + ordering;
        }

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("User-Agent", "VideoGamesCatalogue")
                .build();
        ApiResponseGames responseObject = getResponseGamesList(httpRequest);
        return new PageImpl<>(responseObject.results(), pageable, responseObject.count());
    }

    public ApiResponseFullGameDto getGameById(Long id) {
        String url = BASE_URL + GAME_URL_PART + "/"
                + id + KEY_URL_PART + apiKey;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("User-Agent", "VideoGamesCatalogue")
                .build();
        return getIndividualGame(httpRequest);
    }

    private ApiResponseGames getResponseGamesList(HttpRequest httpRequest) {
        try {
            return objectMapper.readValue(
                    getHttpResponse(httpRequest).body(),
                    ApiResponseGames.class);
        } catch (JacksonException e) {
            log.error("Failed to read httpResponse:", e);
            throw new ObjectMapperException("URL: " + httpRequest.uri()
                    + " Failed to read httpResponse: ", e);
        }
    }

    private ApiResponseFullGameDto getIndividualGame(HttpRequest httpRequest) {
        try {
            return objectMapper.readValue(
                    getHttpResponse(httpRequest).body(),
                    ApiResponseFullGameDto.class);
        } catch (JacksonException e) {
            log.error("Failed to read httpResponse:", e);
            throw new ObjectMapperException("URL: " + httpRequest.uri()
                    + " Failed to read httpResponse: ", e);
        }
    }

    private HttpResponse<String> getHttpResponse(HttpRequest httpRequest) {
        try {
            log.info("Send request to API");
            HttpResponse<String> response = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new HttpResponseException("Received non-200 status code. "
                        + "Response status code: "
                        + response.statusCode());
            }
            return response;
        } catch (IOException | InterruptedException e) {
            throw new ApiException("URL: "
                    + httpRequest.uri()
                    + " Cannot get game(s) from API: ", e);
        }
    }

    private String toRawgOrdering(Sort sort) {
        if (sort.isUnsorted()) {
            return null;
        }
        Sort.Order order = sort.iterator().next();
        String field = order.getProperty();
        Sort.Direction direction = order.getDirection();

        return direction == Sort.Direction.DESC
                ? "-" + field
                : field;
    }
}
