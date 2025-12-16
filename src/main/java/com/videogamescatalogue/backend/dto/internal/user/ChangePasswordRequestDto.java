package com.videogamescatalogue.backend.dto.internal.user;

import com.videogamescatalogue.backend.validation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldMatch(first = "newPassword",
        second = "repeatPassword",
        message = "New password and repeat password must match")
public record ChangePasswordRequestDto(
        @NotBlank(message = "Password is required. Please provide your current password.")
        @Size(min = 8, max = 25, message = "Password must be between 8 and 25 digits")
        String currentPassword,
        @NotBlank(message = "Password is required. Please provide your password.")
        @Size(min = 8, max = 25, message = "Password must be between 8 and 25 digits")
        String newPassword,
        @NotBlank(message = "Please, repeat your password.")
        @Size(min = 8, max = 25, message = "Repeat password must be between 8 and 25 digits")
        String repeatPassword
) {
}
