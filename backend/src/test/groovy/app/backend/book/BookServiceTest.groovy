package app.backend.book

import app.backend.user.RoleEntity
import app.backend.user.UserEntity
import app.backend.user.UserRepository
import app.backend.utils.SecurityContextAccessor
import app.backend.utils.exceptions.ProductAlreadyBorrowedException
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import spock.lang.Specification

import static org.mapstruct.factory.Mappers.getMapper

class BookServiceTest extends Specification {

    BookTypeMapper bookTypeMapper = getMapper(BookTypeMapper)
    BookMapper bookMapper = getMapper(BookMapper).tap {
        bookMapper -> bookMapper.bookTypeMapper = this.bookTypeMapper
    }

    BookRepository bookRepository = Mock()
    BookTypeRepository bookTypeRepository = Mock()

    UserRepository userRepository = Mock()

    SecurityContextAccessor securityContextAccessor = Mock()

    BookService bookService

    def setup() {
        bookService = new BookService(securityContextAccessor, bookRepository, bookMapper, userRepository, bookTypeRepository, bookTypeMapper)
    }

    def "should get all books"() {
        given:
        PageRequest pageRequest = PageRequest.of(0,5)
        1 * bookRepository.findAll(pageRequest) >> getBookEntities()

        when:
        PagedBooksDTO result = bookService.getAllBooks(pageRequest)

        then:
        result == getPagedBooksDTO()

    }

    def "should get all books to borrow"() {
        given:
        PageRequest pageRequest = PageRequest.of(0,5)
        1 * bookRepository.getByCanBeBorrowIsTrueAndAvailableAmountIsGreaterThan(0, pageRequest) >> getBookEntities()

        when:
        PagedBooksDTO result = bookService.getAllBooksToBorrow(pageRequest)

        then:
        result == getPagedBooksDTO()

    }

    def "should get all books by user id"() {
        given:
        int id = 1
        PageRequest pageRequest = PageRequest.of(0,5)

        1 * bookRepository.getByOwnerUsers_Id(id, pageRequest) >> getBookEntities()

        when:
        PagedBooksDTO result = bookService.getByOwnerUser(id, pageRequest)

        then:
        result == getPagedBooksDTO()

    }

    def "should get book by id"() {
        given:
        int id = 1
        1 * bookRepository.findById(id) >> Optional.of(getBookEntity())

        when:
        BookDTO result = bookService.getById(id)

        then:
        result == getBookDTO()
    }

    def "should throw EntityNotFoundException when book was not found by id"() {
        given:
        int id = 1
        1 * bookRepository.findById(id) >> Optional.empty()

        when:
        bookService.getById(id)

        then:
        thrown(EntityNotFoundException)
    }

    def "should get books by sorting criteria - min publish year, max publish year, types"() {
        given:
        PageRequest pageRequest = PageRequest.of(0,5)
        BookSortingCriteriaDTO criteria = new BookSortingCriteriaDTO(Set.of("Type"), 1999, 2020, null, null)

        1 * bookRepository.findByPublishYearBetweenAndType_NameInAndCanBeBorrowIsTrueAndAvailableAmountGreaterThan(criteria.minPublishYear, criteria.maxPublishYear, criteria.types, 0, pageRequest) >> getBookEntities()

        when:
        PagedBooksDTO result = bookService.getBySortingCriteria(criteria, pageRequest)

        then:
        result == getPagedBooksDTO()
    }

    def "should get books by sorting criteria - min publish year, max publish year"() {
        given:
        PageRequest pageRequest = PageRequest.of(0,5)
        BookSortingCriteriaDTO criteria = new BookSortingCriteriaDTO(Set.of(), 1999, 2020, null, null)

        1 * bookRepository.findByPublishYearBetweenAndCanBeBorrowIsTrueAndAvailableAmountGreaterThan(criteria.minPublishYear, criteria.maxPublishYear, 0, pageRequest) >> getBookEntities()

        when:
        PagedBooksDTO result = bookService.getBySortingCriteria(criteria, pageRequest)

        then:
        result == getPagedBooksDTO()
    }

    def "should get all books to remove"() {
        given:
        PageRequest pageRequest = PageRequest.of(0,5)

        1 * bookRepository.findAllWithNoOwnerUser(pageRequest) >> getBookEntities()

        when:
        PagedBooksDTO result = bookService.getAllBooksToRemove(pageRequest)

        then:
        result == getPagedBooksDTO()
    }

    def "should get all books types"() {
        given:
        List<BookTypeEntity> bookTypeEntityList = List.of(
                new BookTypeEntity(1, "TypeOne", new ArrayList<BookEntity>()),
                new BookTypeEntity(2, "TypeTwo", new ArrayList<BookEntity>()),
        )

        1 * bookTypeRepository.findAll() >> bookTypeEntityList

        when:
        List<BookTypeDTO> result = bookService.getAllBookTypes()

        then:
        result == List.of(
                new BookTypeDTO(1, "TypeOne"),
                new BookTypeDTO(2, "TypeTwo")
        )
    }

    def "should save book"() {
        given:
        BookDTO bookToSave = new BookDTO(null, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, new BookTypeDTO(1, "Type"))

        BookTypeEntity bookType = new BookTypeEntity(1, "Type", null)
        BookEntity bookEntity = new BookEntity(null, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, bookType, null)

        1 * bookRepository.save(bookEntity) >> getBookEntity()

        when:
        BookDTO result = bookService.save(bookToSave)

        then:
        result == getBookDTO()
    }

    def "should update book"() {
        given:
        BookDTO updatedBook = getBookDTO()
        1 * bookRepository.findById(updatedBook.id()) >> Optional.of(getBookEntity())

        when:
        BookDTO result = bookService.update(updatedBook)

        then:
        result == getBookDTO()
    }

    def "should throw EntityNotFoundException when updated book doesn't exists in database"() {
        given:
        BookDTO updatedBook = getBookDTO()
        1 * bookRepository.findById(updatedBook.id()) >> Optional.empty()

        when:
        bookService.update(updatedBook)

        then:
        thrown(EntityNotFoundException)
    }

    def "should delete book"() {
        given:
        int id = 1
        1 * bookRepository.findById(id) >> Optional.of(getBookEntity())

        when:
        bookService.delete(id)

        then:
        1 * bookRepository.delete(getBookEntity())
    }

    def "should throw EntityNotFoundException when book to delete doesn't exists"() {
        given:
        int id = 1
        1 * bookRepository.findById(id) >> Optional.empty()

        when:
        bookService.delete(id)

        then:
        thrown(EntityNotFoundException)
    }

    def "should borrow book"() {
        given:
        int id = 1

        when:
        bookService.borrowBook(id)

        then:
        1 * bookRepository.findById(id) >> Optional.of(getBookEntityWithNoOwners())
        1 * securityContextAccessor.getAuthentication() >> getAuthentication()
        1 * userRepository.findByEmail(getAuthentication().getName()) >> Optional.of(getUserEntity())

    }

    def  "should throw EntityNotFoundException when book to borrow doesn't exists"() {
        given:
        int id = 1
        1 * bookRepository.findById(id) >> Optional.empty()

        when:
        bookService.borrowBook(id)

        then:
        thrown(EntityNotFoundException)

    }

    def  "should throw ProductAlreadyBorrowedException when user want borrow the same book second time"() {
        given:
        int id = 1

        1 * bookRepository.findById(id) >> Optional.of(getBookEntity())

        1 * securityContextAccessor.getAuthentication() >> getAuthentication()

        when:
        bookService.borrowBook(id)

        then:
        thrown(ProductAlreadyBorrowedException)

    }

    def "should throw IllegalStateException when user that want borrow book doesn't exists"() {
        given:
        int id = 1

        1 * bookRepository.findById(id) >> Optional.of(getBookEntityWithNoOwners())

        1 * securityContextAccessor.getAuthentication() >> getAuthentication()

        1 * userRepository.findByEmail(getAuthentication().getName()) >> Optional.empty()

        when:
        bookService.borrowBook(id)

        then:
        thrown(IllegalStateException)

    }

    def "should return book" () {
        given:
        int id = 1

        when:
        bookService.returnBook(id)

        then:
        1 * bookRepository.findById(id) >> Optional.of(getBookEntity())
        1 * securityContextAccessor.getAuthentication() >> getAuthentication()

    }

    def "should throw EntityNotFoundException when book to return doesn't exists"() {
        given:
        int id = 1

        1 * bookRepository.findById(id) >> Optional.empty()

        when:
        bookService.returnBook(id)

        then:
        thrown(EntityNotFoundException)
    }

    def "should throw EntityNotFound when user that want return book doesn't exists" () {
        given:
        int id = 1

        1 * bookRepository.findById(id) >> Optional.of(getBookEntityWithNoOwners())

        1 * securityContextAccessor.getAuthentication() >> getAuthentication()

        when:
        bookService.returnBook(id)

        then:
        thrown(EntityNotFoundException)

    }

    private Page<BookEntity> getBookEntities() {
        List<UserEntity> ownerUsers = new ArrayList<>()

        BookTypeEntity bookType = new BookTypeEntity(1, "Type", new ArrayList<BookEntity>())

        List<BookEntity> books = List.of(
                new BookEntity(1, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, bookType, ownerUsers),
                new BookEntity(2, "The Higgler", "A.E. Coppard", 2016, true, 10, bookType, ownerUsers),
                new BookEntity(3, "Sense and Sensibility", "Jane Austen", 1999, true, 5, bookType, ownerUsers)
        )

        Page<BookEntity> pagedBooks = new PageImpl<>(books, PageRequest.of(0,5), books.size())

        return pagedBooks;
    }

    private PagedBooksDTO getPagedBooksDTO() {

        BookTypeDTO bookType = new BookTypeDTO(1, "Type")

        List<BookDTO> books = List.of(
                new BookDTO(1, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, bookType),
                new BookDTO(2, "The Higgler", "A.E. Coppard", 2016, true, 10, bookType),
                new BookDTO(3, "Sense and Sensibility", "Jane Austen", 1999, true, 5, bookType)
        )
        
        return new PagedBooksDTO(1, books)
        
    }

    private BookEntity getBookEntity() {

        List<UserEntity> ownerUsers = new ArrayList<>()
        ownerUsers.add(getUserEntity())

        BookTypeEntity bookType = new BookTypeEntity(1, "Type", new ArrayList<BookEntity>())

        return new BookEntity(1, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, bookType, ownerUsers)
    }

    private BookEntity getBookEntityWithNoOwners() {

        BookTypeEntity bookType = new BookTypeEntity(1, "Type", new ArrayList<BookEntity>())

        return new BookEntity(1, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, bookType, new ArrayList<UserEntity>())
    }

    private BookDTO getBookDTO() {
        BookTypeDTO bookType = new BookTypeDTO(1, "Type")

        return new BookDTO(1, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, bookType)
    }

    private UserEntity getUserEntity() {
        return new UserEntity(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", new ArrayList<RoleEntity>(), new ArrayList<BookEntity>())
    }

    private Authentication getAuthentication () {
        UserDetails principal = new User("anne@gmail.com", "anne123", [])
        return new UsernamePasswordAuthenticationToken(principal, null)
    }

}
