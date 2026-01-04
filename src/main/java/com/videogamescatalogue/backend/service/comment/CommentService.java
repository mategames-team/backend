package com.videogamescatalogue.backend.service.comment;

import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.dto.internal.comment.UpdateCommentRequestDto;
import com.videogamescatalogue.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto create(
            Long gameApiId,
            CreateCommentRequestDto requestDto,
            User user
    );

    Page<CommentDto> getCommentsForGame(Long gameApiId, Pageable pageable);

    Page<CommentDto> getUserComments(Long userId, Pageable pageable);

    CommentDto update(Long commentId, UpdateCommentRequestDto requestDto, Long userId);

    void delete(Long commentId, Long userId);
}
