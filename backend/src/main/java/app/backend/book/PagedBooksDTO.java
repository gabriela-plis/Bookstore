package app.backend.book;

import java.util.List;

public record PagedBooksDTO (
        int totalPages,
        List<BookDTO> books
) {
}
