package app.backend.book;

import app.backend.utils.annotations.ValidYear;
import lombok.Data;

import java.util.Set;

@Data
public class BookSortingCriteriaDTO {

    @ValidYear
    private final Integer minPublishYear;

    @ValidYear
    private final Integer maxPublishYear;

    private final Set<String> types;

    public BookSortingCriteriaDTO(Set<String> types, Integer min, Integer max) {
        this.types = types;
        this.minPublishYear = min;
        this.maxPublishYear = max;
    }
}
