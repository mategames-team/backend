package com.videogamescatalogue.backend.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class GameUpdateScheduler {
    public static final String EVERY_DAY_SIX_AM = "0 0 6 * * ?";
    public static final String ZONE = "Europe/Kyiv";

    private final GameService gameService;

    @Scheduled(
            cron = EVERY_DAY_SIX_AM,
            zone = ZONE
    )
    public void fetchBestGames() {
        log.info("Scheduled fetchBestGames is called");
        gameService.fetchBestGames();
    }
}
