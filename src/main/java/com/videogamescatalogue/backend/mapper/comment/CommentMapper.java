package com.videogamescatalogue.backend.mapper.comment;

import com.videogamescatalogue.backend.config.MapperConfig;
import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.model.Comment;
import com.videogamescatalogue.backend.model.Game;
import com.videogamescatalogue.backend.model.User;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    Comment toModel(CreateCommentRequestDto requestDto);

    @Mapping(source = "user", target = "userId", qualifiedByName = "toUserId")
    CommentDto toDto(Comment comment);

    @Named("toUserId")
    default Long toUserId(User user) {
        return user.getId();
    }
}
