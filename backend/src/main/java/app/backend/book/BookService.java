package app.backend.book;

import app.backend.user.UserEntity;
import app.backend.user.UserRepository;
import app.backend.utils.SecurityContextAccessor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final SecurityContextAccessor securityContextAccessor;

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserRepository userRepository;

    private final BookTypeRepository bookTypeRepository;
    private final BookTypeMapper bookTypeMapper;

    public PagedBooksDTO getAllBooks(int page, int size) {
        Page<BookEntity> pagedBooks = bookRepository.findAll(PageRequest.of(page, size));

        return getPagedBooksDTO(pagedBooks);
    }

    public PagedBooksDTO getAllBooksToBorrow(int page, int size) {
        Page<BookEntity> pagedBooks = bookRepository.getByCanBeBorrowIsTrueAndAvailableAmountIsGreaterThan(0, PageRequest.of(page, size));

        return getPagedBooksDTO(pagedBooks);
    }

    public PagedBooksDTO getByOwnerUser(int id, int page, int size) {
        Page<BookEntity> pagedBooks = bookRepository.getByOwnerUsers_Id(id, PageRequest.of(page, size));

        return getPagedBooksDTO(pagedBooks);
    }

    public BookDTO getById(int id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return bookMapper.toDTO(book);
    }

    public PagedBooksDTO getBySortingCriteria(BookSortingCriteriaDTO criteria, int page, int size) {
        Page<BookEntity> pagedBooks = bookRepository.findByPublishYearBetweenAndType_NameInAndCanBeBorrowIsTrueAndAvailableAmountGreaterThan(criteria.getMinPublishYear(), criteria.getMaxPublishYear(), criteria.getTypes(), 0, PageRequest.of(page, size));

        return getPagedBooksDTO(pagedBooks);
    }

    public PagedBooksDTO getAllBooksToRemove(int page, int size) {
        Page<BookEntity> pagedBooks = bookRepository.findAllWithNoOwnerUser(PageRequest.of(page, size));

        return getPagedBooksDTO(pagedBooks);
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
    public void borrowBook(int bookId) {
        BookEntity book = bookRepository.findById(bookId)
            .orElseThrow(EntityNotFoundException::new);

        String username = securityContextAccessor.getAuthentication().getName();

        boolean isAlreadyBorrowed = book.getOwnerUsers().stream().anyMatch(user -> user.getEmail().equals(username));
        if(isAlreadyBorrowed) {
            return;
        }

        book.getOwnerUsers().add(userRepository.findByEmail(username).orElseThrow(IllegalStateException::new));

        book.setAvailableAmount(book.getAvailableAmount()-1);
    }

    @Transactional
    public void returnBook(int bookId) {
        BookEntity book = bookRepository.findById(bookId)
            .orElseThrow(EntityNotFoundException::new);

        String username = securityContextAccessor.getAuthentication().getName();

        UserEntity user = book.getOwnerUsers().stream()
            .filter(u -> u.getEmail().equals(username))
            .findAny()
            .orElseThrow(EntityNotFoundException::new);

        book.getOwnerUsers().remove(user);

        book.setAvailableAmount(book.getAvailableAmount()+1);
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

    private PagedBooksDTO getPagedBooksDTO(Page<BookEntity> pagedBooks) {
        List<BookDTO> books = bookMapper.toDTOs(pagedBooks.getContent());

        return new PagedBooksDTO(pagedBooks.getTotalPages(), books);
    }

}
