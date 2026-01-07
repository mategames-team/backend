package com.videogamescatalogue.backend.controller;

import com.videogamescatalogue.backend.dto.internal.user.ChangePasswordRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UpdateUserRequestDto;
import com.videogamescatalogue.backend.dto.internal.user.UserResponseDto;
import com.videogamescatalogue.backend.exception.AccessNotAllowedException;
import com.videogamescatalogue.backend.model.User;
import com.videogamescatalogue.backend.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "Operations related to user profiles and accounts")
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Get user information",
            description = """
                    Returns user profile information. 
                    If id is provided, returns info by id.
                    If id is not provided, returns info about authenticated user.
                    Does not require authentication
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "User information retrieved successfully",
            content = @Content(
                    schema = @Schema(implementation = UserResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
    )
    @GetMapping("/info")
    public UserResponseDto getUserInfo(
            @RequestParam(required = false) Long id,
            @AuthenticationPrincipal User user
    ) {
        return userService.getUserInfo(id, user);
    }

    @Operation(
            summary = "Update authenticated user's profile",
            description = "Updates profile information of the currently authenticated user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User profile updated successfully",
            content = @Content(
                    schema = @Schema(implementation = UserResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authenticated",
            content = @Content
    )
    @PatchMapping("/me")
    public UserResponseDto updateUserInfo(
            @Valid @RequestBody UpdateUserRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return userService.updateUserInfo(requestDto, user);
    }

    @Operation(
            summary = "Change authenticated user's password",
            description = "Changes the password of the currently authenticated user. "
                    + "This operation requires the current password and a new password."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Password changed successfully",
            content = @Content(
                    schema = @Schema(implementation = UserResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid current password or validation error",
            content = @Content
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authenticated",
            content = @Content
    )
    @PatchMapping("/me/password")
    public UserResponseDto changePassword(
            @Valid @RequestBody ChangePasswordRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return userService.changePassword(requestDto, user);
    }
}
