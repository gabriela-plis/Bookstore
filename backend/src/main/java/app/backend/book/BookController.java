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
    public PagedBooksDTO getAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5", required = false) int size) {
        return service.getAllBooks(page, size);
    }

    @GetMapping("/to-borrow")
    public PagedBooksDTO getAllBooksToBorrow(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5", required = false) int size) {
        return service.getAllBooksToBorrow(page, size);
    }

    @GetMapping("/to-remove")
    public PagedBooksDTO getAllBooksToRemove(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5", required = false) int size) {
        return service.getAllBooksToRemove(page, size);
    }

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable int id) {
       return service.getById(id);
    }

    @GetMapping("/user/{id}")
    public PagedBooksDTO getUserBooks(@PathVariable int id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5", required = false) int size) {
       return service.getByOwnerUser(id, page, size);
    }

    @GetMapping("/criteria")
    public PagedBooksDTO getSortedBooks(@Valid BookSortingCriteriaDTO criteria, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5", required = false) int size) {
       return service.getBySortingCriteria(criteria, page, size);
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
