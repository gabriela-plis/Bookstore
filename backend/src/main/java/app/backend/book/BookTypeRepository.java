package app.backend.book;

import org.springframework.data.repository.ListCrudRepository;

public interface BookTypeRepository extends ListCrudRepository<BookTypeEntity, Integer> {
}
