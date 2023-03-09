package app.backend.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDTO(

        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotNull
        Boolean employee

) {
}