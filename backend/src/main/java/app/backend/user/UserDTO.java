package app.backend.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
    Integer id,

    @NotBlank
    String firstName,

    @NotBlank
    String lastName,

    @NotBlank
    String phone,

    String email,

    @NotBlank
    String password,

    @NotNull
    Boolean employee
) { }
