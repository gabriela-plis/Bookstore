package app.backend.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookDTO(
    Integer id,

    @NotBlank
    String title,

    @NotBlank
    String author,

    Integer publishYear,

    @NotNull
    Boolean canBeBorrow,

    Integer availableAmount,

    BookTypeDTO bookType
) {
}
