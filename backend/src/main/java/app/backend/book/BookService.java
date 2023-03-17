package app.backend.book;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    public List<BookDTO> getAllBooks() {
        List<BookEntity> books = repository.findAll();
        return mapper.toDTOs(books);
    }

    public List<BookDTO> findByUserId(int id) {
        List<BookEntity> books = repository.getByOwnerUsers_Id(id).orElseThrow(EntityNotFoundException::new);
        return mapper.toDTOs(books);
    }

    public BookDTO findById(int id) {
        BookEntity book = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return mapper.toDTO(book);
    }

    public List<BookDTO> findBySortingCriteria(BookSortingCriteria criteria) {
        List<BookEntity> books = repository.findByBookType_NameAndPublishYearBetween(criteria.getType(), criteria.getMinPublishYear(), criteria.getMaxPublishYear()).orElseThrow(EntityNotFoundException::new);
        return mapper.toDTOs(books);
    }
}
