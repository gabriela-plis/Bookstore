package app.backend.book;

import lombok.Data;

@Data
public class BookSortingCriteria {
    private final Integer minPublishYear;
    private final Integer maxPublishYear;
    private final String typeName;

    public BookSortingCriteria(String type, Integer min, Integer max) {
        this.typeName = type;
        this.minPublishYear = min;
        this.maxPublishYear = max;
    }
}
