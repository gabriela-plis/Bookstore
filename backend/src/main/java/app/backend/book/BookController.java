package app.backend.book;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
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

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable int id) {
       return service.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<BookDTO> getUserBooks(@PathVariable int userId) {
       return service.findByUserId(userId);
    }

    @GetMapping("/criteria")
    public List<BookDTO> getSortedBooks(BookSortingCriteria criteria) {
       return service.findBySortingCriteria(criteria);
    }

    @PostMapping
    public BookDTO add(@RequestBody @Valid BookDTO book) {
        return service.save(book);
    }

    @PutMapping
    public BookDTO update(@RequestBody @Valid BookDTO book) {
        return service.update(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

}
