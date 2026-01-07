package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.comment.CommentDto;
import com.videogamescatalogue.backend.dto.internal.comment.CreateCommentRequestDto;
import com.videogamescatalogue.backend.dto.internal.comment.UpdateCommentRequestDto;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comments", description = "Create, update, delete, and view comments for games")
@RequiredArgsConstructor
@RequestMapping
@RestController
public class CommentController {
    private static final int DEFAULT_PAGE_SIZE = 30;
    private final CommentService commentService;

    @Operation(
            summary = "Create a comment for a game",
            description = """
    Creates a new comment for the specified game. 
    User must be authenticated.
            """
    )
    @ApiResponse(
            responseCode = "201",
            description = "Comment created successfully",
            content = @Content(schema = @Schema(implementation = CommentDto.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authenticated",
            content = @Content
    )
    @PostMapping("/comments/games/{gameApiId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto create(
            @PathVariable Long gameApiId,
            @RequestBody @Valid CreateCommentRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return commentService.create(gameApiId, requestDto, user);
    }

    @Operation(
            summary = "Get comments for a game",
            description = "Returns paginated comments for a specific game"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comments retrieved successfully",
            content = @Content(schema = @Schema(implementation = CommentDto.class))
    )
    @GetMapping("/games/{gameApiId}/comments")
    public Page<CommentDto> getCommentsForGame(
            @PathVariable Long gameApiId,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        return commentService.getCommentsForGame(gameApiId, pageable);
    }

    @Operation(
            summary = "Get usercomments",
            description = "Returns paginated comments created by "
                    + "the authenticated user or a user by id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User comments retrieved successfully",
            content = @Content(schema = @Schema(implementation = CommentDto.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authenticated",
            content = @Content
    )
    @GetMapping("/users/info/comments")
    public Page<CommentDto> getUserComments(
            @RequestParam(required = false) Long id,
            @AuthenticationPrincipal User user,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        return commentService.getUserComments(user, id, pageable);
    }

    @Operation(
            summary = "Get authenticated user's comments",
            description = "Returns paginated comments created by the authenticated user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User comments retrieved successfully",
            content = @Content(schema = @Schema(implementation = CommentDto.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authenticated",
            content = @Content
    )
    @PatchMapping("/comments/{commentId}")
    public CommentDto update(
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return commentService.update(commentId,requestDto, user.getId());
    }

    @Operation(
            summary = "Delete a comment",
            description = "Deletes a comment owned by the authenticated user"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Comment deleted successfully"
    )
    @ApiResponse(
            responseCode = "403",
            description = "User is not the owner of the comment",
            content = @Content
    )
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user
    ) {
        commentService.delete(commentId, user.getId());
    }
}
