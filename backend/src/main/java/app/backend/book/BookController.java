package app.backend.book;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    @GetMapping()
    public List<BookDTO> getAllBooks () {
        return service.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookDTO getBook (@PathVariable int id) {
       return service.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<BookDTO> getUserBooks (@PathVariable int userId) {
       return service.findByUserId(userId);
    }

    @GetMapping("/criteria")
    public List<BookDTO> getSortedBooks (BookSortingCriteria criteria) {
        System.out.println(criteria);
       return service.findBySortingCriteria(criteria);
    }
}
