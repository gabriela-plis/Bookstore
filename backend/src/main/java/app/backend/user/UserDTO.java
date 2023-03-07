package app.backend.user;


public record UserDTO(
    Integer id,
    String firstName,
    String lastName,
    String phone,
    String email,
    String password,
    boolean employee
) { }
