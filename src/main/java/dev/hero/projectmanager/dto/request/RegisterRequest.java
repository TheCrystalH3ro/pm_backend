package dev.hero.projectmanager.dto.request;

import dev.hero.projectmanager.validation.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatch
public record RegisterRequest(
        @NotBlank(message = "Username cannot be empty")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,

        @NotBlank(message = "Please confirm your password")
        String confirmPassword
) {
}
