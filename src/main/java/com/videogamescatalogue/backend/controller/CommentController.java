package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("games/{id}/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto create(
            @PathVariable Long id,
            @RequestBody @Valid CreateCommentRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return commentService.create(id, requestDto, user);
    }
}
