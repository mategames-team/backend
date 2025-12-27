package com.videogamescatalogue.backend.mapper.usergame;

import com.videogamescatalogue.backend.dto.internal.game.GameDto;
import com.videogamescatalogue.backend.mapper.game.GameMapper;
import com.videogamescatalogue.backend.model.Game;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameProvider {
    private final GameMapper gameMapper;

    @Named("toGameDto")
    public GameDto toGameDto(Game game) {
        return gameMapper.toDto(game);
    }
}
