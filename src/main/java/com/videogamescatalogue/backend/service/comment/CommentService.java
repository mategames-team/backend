package com.videogamescatalogue.backend.service.comment;

import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

public interface CommentService {
    CommentDto create(
            Long gameApiId,
            CreateCommentRequestDto requestDto,
            User user
    );

    Page<CommentDto> getCommentsForGame(Long id, Pageable pageable);

    void delete(Long commentId, Long userId);
}
