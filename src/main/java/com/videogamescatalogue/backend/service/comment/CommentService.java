package com.videogamescatalogue.backend.service.comment;

import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface CommentService {
    CommentDto create(
            Long gameApiId,
            CreateCommentRequestDto requestDto,
            User user
    );
}
