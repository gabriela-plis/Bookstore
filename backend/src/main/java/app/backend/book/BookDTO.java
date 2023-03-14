package app.backend.book;

public record BookDTO(
        Integer id,
        String title,
        String author,
        Integer publishYear,
        Integer availableAmount,
        String type
) { }
