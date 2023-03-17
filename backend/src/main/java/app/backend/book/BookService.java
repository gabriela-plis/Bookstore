package app.backend.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    public List<BookDTO> getAllBooks() {
        return null;
    }

    public BookDTO findById() {
        return null;
    }

    public List<BookDTO> findBySortingCriteria() {
        return null;
    }


}
