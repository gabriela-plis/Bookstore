package app.backend.book;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    private final BookTypeRepository bookTypeRepository;
    private final BookTypeMapper bookTypeMapper;


    public List<BookDTO> getAllBooks() {
        List<BookEntity> books = bookRepository.findAll();

        return bookMapper.toDTOs(books);
    }

    public List<BookDTO> getAllBooksToBorrow() {
        List<BookEntity> books = bookRepository.getByCanBeBorrowIsTrueAndAvailableAmountIsGreaterThan(0);

        return bookMapper.toDTOs(books);
    }

    public List<BookDTO> findByUserId(int id) {
        List<BookEntity> books = bookRepository.getByOwnerUsers_Id(id);

        return bookMapper.toDTOs(books);
    }

    public BookDTO findById(int id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return bookMapper.toDTO(book);
    }

    public List<BookDTO> getBySortingCriteria(BookSortingCriteriaDTO criteria) {
        List<BookEntity> books = bookRepository.findByPublishYearBetweenAndBookType_NameIgnoreCaseAndCanBeBorrowIsTrue(criteria.getMinPublishYear(), criteria.getMaxPublishYear(), criteria.getTypeName());

        return bookMapper.toDTOs(books);
    }

    public List<BookDTO> getAllBooksToRemove() {
        List<BookEntity> books = bookRepository.findAllWithNoOwnerUser();

        return bookMapper.toDTOs(books);
    }

    public List<BookTypeDTO> getAllBookTypes() {
        List<BookTypeEntity> bookTypes = bookTypeRepository.findAll();

        return bookTypeMapper.toDTOs(bookTypes);
    }

    public BookDTO save(BookDTO bookToSave) {
        BookEntity savedBook = bookRepository.save(bookMapper.toEntity(bookToSave));

        return bookMapper.toDTO(savedBook);
    }

    @Transactional
    public BookDTO update(BookDTO updatedBook) {
        BookEntity bookEntity = bookRepository.findById(updatedBook.id())
            .orElseThrow(EntityNotFoundException::new);

        bookMapper.updateEntity(bookEntity, updatedBook);

        return bookMapper.toDTO(bookEntity);
    }

    public void delete(int id) {
        BookEntity bookToDelete = bookRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        bookRepository.delete(bookToDelete);
    }


}
