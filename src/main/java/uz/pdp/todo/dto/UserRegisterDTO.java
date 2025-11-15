package uz.pdp.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(
        @NotBlank @Size(min = 4) String username,
        @NotBlank @Size(min = 6) String password
) {
}

