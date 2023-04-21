package app.backend.book;

import app.backend.utils.annotations.ValidYear;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookSortingCriteriaDTO {

    @ValidYear
    private final Integer minPublishYear;

    @ValidYear
    private final Integer maxPublishYear;

    @NotBlank
    private final String typeName;

    public BookSortingCriteriaDTO(String type, Integer min, Integer max) {
        this.typeName = type;
        this.minPublishYear = min;
        this.maxPublishYear = max;
    }
}
