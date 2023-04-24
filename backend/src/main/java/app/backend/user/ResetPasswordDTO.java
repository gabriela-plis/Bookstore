package app.backend.user;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDTO(

    @NotBlank
    String oldPassword,

    @NotBlank
    String newPassword

) { }
