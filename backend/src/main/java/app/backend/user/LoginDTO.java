package app.backend.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginDTO(

        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotNull
        Boolean isEmployee

) {
}