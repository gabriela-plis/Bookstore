package app.backend.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Page<BookEntity> getByCanBeBorrowIsTrueAndAvailableAmountIsGreaterThan(Integer availableAmount, Pageable paging);

    Page<BookEntity> getByOwnerUsers_Id(int id, Pageable paging);

    Page<BookEntity> findByPublishYearBetweenAndType_NameInAndCanBeBorrowIsTrueAndAvailableAmountGreaterThan(Integer publishYearMin, Integer publishYearMax, Set<String>typeName, Integer availableAmountGreaterThan, Pageable paging);

    @Query(
        value = """
                SELECT book
                FROM BookEntity book
                WHERE size(book.ownerUsers) = 0
            """
    )
    Page<BookEntity> findAllWithNoOwnerUser(Pageable pageable);
}
