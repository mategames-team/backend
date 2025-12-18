package com.videogamescatalogue.backend.mapper.comment;

import com.videogamescatalogue.backend.config.MapperConfig;
import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    @Mapping(target = "localDateTime", defaultExpression = "java(java.time.LocalDateTime.now())")
    Comment toModel(CreateCommentRequestDto requestDto);

    CommentDto toDto(Comment comment);
}
