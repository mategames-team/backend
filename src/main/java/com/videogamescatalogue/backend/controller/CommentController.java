package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.dto.internal.comment.UpdateCommentRequestDto;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PostMapping("/comments/games/{gameApiId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto create(
            @PathVariable Long gameApiId,
            @RequestBody @Valid CreateCommentRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return commentService.create(gameApiId, requestDto, user);
    }

    @GetMapping("/games/{gameApiId}/comments")
    public Page<CommentDto> getCommentsForGame(@PathVariable Long gameApiId, Pageable pageable) {
        return commentService.getCommentsForGame(gameApiId, pageable);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentDto update(
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return commentService.update(commentId,requestDto, user.getId());
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user
    ) {
        commentService.delete(commentId, user.getId());
    }
}
