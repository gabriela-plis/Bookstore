package app.backend.user;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
    Integer id,
    String firstName,
    String lastName,
    String phone,
    String email,
    String password,
    Boolean employee
) { }
