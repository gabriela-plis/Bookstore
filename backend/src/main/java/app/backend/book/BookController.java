package app.backend.book;

import app.backend.utils.annotations.ValidIdAbsence;
import app.backend.utils.annotations.ValidIdPresence;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public List<BookDTO> getAllBooks() {
        return service.getAllBooks();
    }

    @GetMapping("/to-borrow")
    public List<BookDTO> getAllBooksToBorrow() {
        return service.getAllBooksToBorrow();
    }

    @GetMapping("/to-remove")
    public List<BookDTO> getAllBooksToRemove() {
        return service.getAllBooksToRemove();
    }

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable int id) {
       return service.getById(id);
    }

    @GetMapping("/user/{userEmail}")
    public List<BookDTO> getUserBooks(@PathVariable String userEmail) {
       return service.getByOwnerUser(userEmail);
    }

    @GetMapping("/criteria")
    public List<BookDTO> getSortedBooks(@Valid BookSortingCriteriaDTO criteria) {
       return service.getBySortingCriteria(criteria);
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
