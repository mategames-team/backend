package com.videogamescatalogue.backend;

import com.videogamescatalogue.backend.service.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@RequiredArgsConstructor
@SpringBootApplication
public class VideogamescatalogueBackendApplication implements CommandLineRunner {
    private final GameService gameService;

    public static void main(String[] args) {
        SpringApplication.run(VideogamescatalogueBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        gameService.fetchFromDb();
    }
}
