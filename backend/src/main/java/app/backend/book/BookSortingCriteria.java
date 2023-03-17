package app.backend.book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookSortingCriteria {
    Integer publishYear;
    String type;
}
