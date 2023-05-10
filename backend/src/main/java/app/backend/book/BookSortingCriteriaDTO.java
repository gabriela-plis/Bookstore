package app.backend.book;

import app.backend.utils.annotations.ValidYear;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class BookSortingCriteriaDTO {

    @ValidYear
    private final Integer minPublishYear;

    @ValidYear
    private final Integer maxPublishYear;

    @NotNull
    private final Set<String> types;

    public BookSortingCriteriaDTO(Set<String> types, Integer min, Integer max, Integer minPublishYear, Integer maxPublishYear) {
        this.types = types;

        if (min != null && max != null ) {
            this.minPublishYear = min;
            this.maxPublishYear = max;
        } else {
            this.minPublishYear = minPublishYear;
            this.maxPublishYear = maxPublishYear;
        }
    }
}
