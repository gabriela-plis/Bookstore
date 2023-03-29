package app.backend.security;

public record LoginDTO(
    String email,
    String password
) {
}