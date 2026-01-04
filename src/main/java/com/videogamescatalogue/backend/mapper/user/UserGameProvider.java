package com.videogamescatalogue.backend.mapper.user;

import com.videogamescatalogue.backend.dto.internal.usergame.UserGameStatusDto;
import com.videogamescatalogue.backend.mapper.usergame.UserGameMapper;
import com.videogamescatalogue.backend.model.UserGame;
import com.videogamescatalogue.backend.repository.UserGameRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserGameProvider {
    private final UserGameRepository userGameRepository;
    private final UserGameMapper userGameMapper;

    @Named("getStatusDtoList")
    public List<UserGameStatusDto> getStatusDtoList(Long userId) {
        List<UserGame> userGames = userGameRepository.findAllByUserId(userId);
        return userGameMapper.toStatusDtoList(userGames);
    }
}
