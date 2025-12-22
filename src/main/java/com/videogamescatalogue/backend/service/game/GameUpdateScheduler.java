package com.videogamescatalogue.backend.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class GameUpdateScheduler {
    public static final String EVERY_DAY = "0 0 9 * * ?";
    public static final String EVERY_SUNDAY = "0 0 0 * * SUN";
    public static final String EVERY_MINUTE = "0 * * * * *";
    public static final String EVERY_THREE_MINUTES = "0 */3 * * * *";
    public static final String ZONE = "Europe/Kyiv";
    private static final String INITIAL_DELAY = "PT24H";

    private final GameService gameService;

    @Scheduled(cron = EVERY_MINUTE, zone = ZONE,
            initialDelayString = "PT5M"
    )
    public void fetchBestGames() {
        log.info("Scheduled fetchBestGames is called");
        gameService.fetchBestGames();
    }
}
