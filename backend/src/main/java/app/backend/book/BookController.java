package app.backend.book;

import app.backend.utils.annotations.ValidIdAbsence;
import app.backend.utils.annotations.ValidIdPresence;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Validated
public class BookController {

    private final BookService service;

    @GetMapping
    public PagedBooksDTO getAllBooks(@PageableDefault(size = 5) Pageable pageable) {
        return service.getAllBooks(pageable);
    }

    @GetMapping("/to-borrow")
    public PagedBooksDTO getAllBooksToBorrow(@PageableDefault(size = 5) Pageable pageable) {
        return service.getAllBooksToBorrow(pageable);
    }

    @GetMapping("/to-remove")
    public PagedBooksDTO getAllBooksToRemove(@PageableDefault(size = 5) Pageable pageable) {
        return service.getAllBooksToRemove(pageable);
    }

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable int id) {
       return service.getById(id);
    }

    @GetMapping("/user/{id}")
    public PagedBooksDTO getUserBooks(@PathVariable int id, @PageableDefault(size = 5) Pageable pageable) {
       return service.getByOwnerUser(id, pageable);
    }

    @GetMapping("/criteria")
    public PagedBooksDTO getSortedBooks(@Valid BookSortingCriteriaDTO criteria, @PageableDefault(size = 5) Pageable pageable) {
       return service.getBySortingCriteria(criteria, pageable);
    }

    @GetMapping("/types")
    public List<BookTypeDTO> getBookTypes() {
        return service.getAllBookTypes();
    }

    @PostMapping
    public BookDTO add(@RequestBody @Valid @ValidIdAbsence BookDTO book) {
        return service.save(book);
    }

    @PostMapping("/{bookId}/borrow")
    public void borrowBook(@PathVariable int bookId) {
        service.borrowBook(bookId);
    }

    @PostMapping("/{bookId}/return")
    public void returnBook(@PathVariable int bookId) {
        service.returnBook(bookId);
    }

    @PutMapping
    public BookDTO update(@RequestBody @Valid @ValidIdPresence BookDTO book) {
        return service.update(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

}
