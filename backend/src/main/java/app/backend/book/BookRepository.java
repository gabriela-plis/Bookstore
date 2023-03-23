package app.backend.book;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BookRepository extends ListCrudRepository<BookEntity, Integer> {

    List<BookEntity> getByCanBeBorrowIsTrueAndAvailableAmountIsGreaterThan(Integer availableAmount);

    List<BookEntity> getByOwnerUsers_Id(int userId);

    List<BookEntity> findByPublishYearBetweenAndBookType_NameIgnoreCaseAndCanBeBorrowIsTrue(Integer publishYearMin, Integer publishYearMax, String typeName);

    @Query(
        value = """
                SELECT book
                FROM BookEntity book
                WHERE size(book.ownerUsers) = 0
            """
    )
    List<BookEntity> findAllWithNoOwnerUser();
}
