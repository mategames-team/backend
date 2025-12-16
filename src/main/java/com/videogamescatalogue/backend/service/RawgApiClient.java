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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RawgApiClient {
    private static final String BASE_URL = "https://api.rawg.io/api/";
    private static final String GAME_URL_PART = "games";
    private static final String KEY_URL_PART = "?key=";
    private static final String PAGE_NUMBER_URL_PART = "&page=";
    private static final String PAGE_SIZE_URL_PART = "&page_size=30";
    private static final String ORDERING_URL_PART = "&ordering=-added";
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

        for (int i = 1; i < 2; i++) {
            String url = BASE_URL + GAME_URL_PART
                    + KEY_URL_PART + apiKey
                    + ORDERING_URL_PART
                    + EXCLUDE_ADDITIONS_URL_PART
                    + DATES_BETWEEN_URL_PART
                    + METACRITIC_URL_PART
                    + PAGE_SIZE_URL_PART
                    + PAGE_NUMBER_URL_PART + i;
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();
            ApiResponseGames responseObject = getResponseGamesList(httpRequest);

            result.addAll(responseObject.results());
        }

        return result;
    }

    public ApiResponseFullGameDto getGameById(Long id) {
        String url = BASE_URL + GAME_URL_PART + "/"
                + id + KEY_URL_PART + apiKey;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();
        return getIndividualGame(httpRequest);
    }

    private ApiResponseGames getResponseGamesList(HttpRequest httpRequest) {
        try {
            return objectMapper.readValue(
                    getHttpResponse(httpRequest).body(),
                    ApiResponseGames.class);
        } catch (JacksonException e) {
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
            throw new ObjectMapperException("URL: " + httpRequest.uri()
                    + " Failed to read httpResponse: ", e);
        }
    }

    private HttpResponse<String> getHttpResponse(HttpRequest httpRequest) {
        try {
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
                    + " Cannot get all game(s) from API: ", e);
        }
    }
}
