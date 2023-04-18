package app.backend.user;

import jakarta.validation.constraints.NotBlank;

public record RegisteredUserDTO(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        String phone,

        @NotBlank
        String email,

        @NotBlank
        String password
) { }
