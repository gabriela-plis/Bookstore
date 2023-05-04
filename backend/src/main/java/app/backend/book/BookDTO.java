package app.backend.book;

import app.backend.utils.annotations.ValidYear;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record BookDTO(

    Integer id,

    @NotBlank
    String title,

    @NotBlank
    String author,

    @ValidYear
    Integer publishYear,

    @NotNull
    Boolean canBeBorrow,

    @Min(0)
    @Max(100)
    @NotNull
    Integer availableAmount,

    @Valid
    BookTypeDTO type
) {
}
