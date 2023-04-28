package app.backend.book;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Set;

public interface BookRepository extends ListCrudRepository<BookEntity, Integer> {

    @Query("SELECT b from BookEntity b JOIN FETCH b.type")
    List<BookEntity> findAll();

    List<BookEntity> getByCanBeBorrowIsTrueAndAvailableAmountIsGreaterThan(Integer availableAmount);

    List<BookEntity> getByOwnerUsers_Email(String email);

    List<BookEntity> findByPublishYearBetweenAndType_NameInAndCanBeBorrowIsTrueAndAvailableAmountGreaterThan(Integer publishYearMin, Integer publishYearMax, Set<String>typeName, Integer availableAmountGreaterThan);

    @Query(
        value = """
                SELECT book
                FROM BookEntity book
                WHERE size(book.ownerUsers) = 0
            """
    )
    List<BookEntity> findAllWithNoOwnerUser();
}
