package uz.pdp.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record UserRegisterDTO(
        @NotBlank
        @Size(min = 4, max = 20, message = "Username must be 4–20 characters")
        String username,

        @NotBlank
        @Size(min = 6, max = 50, message = "Password must be 6–50 characters")
        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain uppercase, lowercase, number, and special character"
        )
        String password
) {}
