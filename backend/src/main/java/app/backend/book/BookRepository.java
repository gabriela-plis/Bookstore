package app.backend.book;

import org.springframework.data.repository.ListCrudRepository;

public interface BookRepository extends ListCrudRepository<BookEntity, Integer> {
}
