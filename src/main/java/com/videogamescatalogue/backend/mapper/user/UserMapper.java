package com.videogamescatalogue.backend.mapper.user;

import com.videogamescatalogue.backend.config.MapperConfig;
import com.videogamescatalogue.backend.dto.internal.user.UpdateUserRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserRegistrationRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserRegistrationResponseDto;
import com.videogamescatalogue.backend.dto.internal.user.UserResponseDto;
import com.videogamescatalogue.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = UserGameProvider.class)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toModel(UserRegistrationRequestDto requestDto);

    @Mapping(source = "id", target = "userGames", qualifiedByName = "getStatusDtoList")
    UserResponseDto toDto(User user);

    @Mapping(source = "token", target = "token")
    UserRegistrationResponseDto toRegistrationResponseDto(User user, String token);

    User updateProfileInfo(@MappingTarget User user, UpdateUserRequestDto requestDto);
}
