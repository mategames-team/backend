package com.videogamescatalogue.backend.dto.internal.user;

import com.videogamescatalogue.backend.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@FieldMatch(first = "password",
        second = "repeatPassword",
        message = "Password and repeat password must match")
public record UserRegistrationRequestDto(
        @NotBlank(message = "Username is required. Please provide username.")
        @Pattern(regexp = "^[A-Za-z0-9_]{3,20}$",
                message = "Profile name may contain only letters, digits, and underscore "
                        + "and be between 3 and 20 digits")
        String profileName,
        @NotBlank(message = "Password is required. Please provide your password.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])"
                + "[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,30}$",
                message = "Password must include uppercase, digit, and special character"
                        + "and be between 8 and 30 digits")
        String password,
        @NotBlank(message = "Please, repeat your password.")
        @Size(min = 8, max = 30, message = "Repeat password must be between 8 and 30 digits")
        String repeatPassword,
        @Email(message = "Invalid format of email.",
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+\\.com$")
        @NotBlank(message = "Email is required. Please provide your email.")
        @Size(min = 14, max = 72, message = "Email must be between 14 and 72 digits")
        String email,
        @Size(max = 5000, message = "About user info must be less than 5000 digits")
        String about,
        @Size(max = 50, message = "Location info must be less than 50 digits")
        String location
) {
}
