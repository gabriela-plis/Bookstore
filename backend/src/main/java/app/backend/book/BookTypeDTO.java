package app.backend.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookTypeDTO(
    @Min(1)
    Integer id,

    @NotBlank
    String name
) {
}
