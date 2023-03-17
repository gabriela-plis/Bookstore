package app.backend.book;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends ListCrudRepository<BookEntity, Integer> {
    Optional<List<BookEntity>> getByOwnerUsers_Id(int userId);

    Optional<List<BookEntity>> findByBookType_NameAndPublishYearBetween(String typeName, Integer publishYearMin, Integer publishYearMax);
}
