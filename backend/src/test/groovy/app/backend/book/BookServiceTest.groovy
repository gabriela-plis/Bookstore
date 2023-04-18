package app.backend.book

import app.backend.user.RoleEntity
import app.backend.user.UserEntity
import app.backend.user.UserRepository
import app.backend.user.UserService
import app.backend.utils.SecurityContextAccessor
import jakarta.persistence.EntityNotFoundException
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

    SecurityContextAccessor securityContextAccessor = Mock()

    BookService bookService

    def setup() {
        bookService = new BookService(securityContextAccessor, bookRepository, bookMapper, bookTypeRepository, bookTypeMapper)
    }

    def "should get all books"() {
        given:
        1 * bookRepository.findAll() >> getBookEntityList()

        when:
        List<BookDTO> result = bookService.getAllBooks()

        then:
        result == getBookDTOList()

    }

    def "should get all books to borrow"() {
        given:
        1 * bookRepository.getByCanBeBorrowIsTrueAndAvailableAmountIsGreaterThan(0) >> getBookEntityList()

        when:
        List<BookDTO> result = bookService.getAllBooksToBorrow()

        then:
        result == getBookDTOList()

    }

    def "should get all books by user id"() {
        given:
        int id = 1
        1 * bookRepository.getByOwnerUsers_Id(id) >> getBookEntityList()

        when:
        List<BookDTO> result = bookService.getByOwnerUserId(id)

        then:
        result == getBookDTOList()

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

    def "should  throw EntityNotFoundException when book was not found by id"() {
        given:
        int id = 1
        1 * bookRepository.findById(id) >> Optional.empty()

        when:
        bookService.getById(id)

        then:
        thrown(EntityNotFoundException)
    }

    def "should get books by sorting criteria"() {
        given:
        BookSortingCriteriaDTO criteria = new BookSortingCriteriaDTO("Type", 1999, 2020)
        1 * bookRepository.findByPublishYearBetweenAndBookType_NameIgnoreCaseAndCanBeBorrowIsTrue(criteria.minPublishYear, criteria.maxPublishYear, criteria.typeName) >> getBookEntityList()

        when:
        List<BookDTO> result = bookService.getBySortingCriteria(criteria)

        then:
        result == getBookDTOList()
    }

    def "should get all books to remove"() {
        given:
        1 * bookRepository.findAllWithNoOwnerUser() >> getBookEntityList()

        when:
        List<BookDTO> result = bookService.getAllBooksToRemove()

        then:
        result == getBookDTOList()
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
        1 * bookRepository.findById(id) >> Optional.of(getBookEntity())
        1 * securityContextAccessor.getAuthentication() >> getAuthentication()

    }

    def  "should throw EntityNotFoundException when book to borrow doesn't exists"() {
        given:
        int id = 1
        bookRepository.findById(id) >> Optional.empty()

        when:
        bookService.borrowBook(id)

        then:
        thrown(EntityNotFoundException)

    }

    def "should throw EntityNotFoundException when user that want borrow book doesn't exists"() {
        given:
        int id = 1

         1 * bookRepository.findById(id) >> Optional.of(getBookEntity())

        1 * securityContextAccessor.getAuthentication() >> getNonExistentUserAuthentication()

        when:
        bookService.borrowBook(id)

        then:
        thrown(EntityNotFoundException)

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

    private List<BookEntity> getBookEntityList() {
        List<UserEntity> ownerUsers = new ArrayList<>()

        BookTypeEntity bookType = new BookTypeEntity(1, "Type", new ArrayList<BookEntity>())

        return List.of(
                new BookEntity(1, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, bookType, ownerUsers),
                new BookEntity(2, "The Higgler", "A.E. Coppard", 2016, true, 10, bookType, ownerUsers),
                new BookEntity(3, "Sense and Sensibility", "Jane Austen", 1999, true, 5, bookType, ownerUsers)
        )
    }

    private List<BookDTO> getBookDTOList() {

        BookTypeDTO bookType = new BookTypeDTO(1, "Type")

        return List.of(
                new BookDTO(1, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, bookType),
                new BookDTO(2, "The Higgler", "A.E. Coppard", 2016, true, 10, bookType),
                new BookDTO(3, "Sense and Sensibility", "Jane Austen", 1999, true, 5, bookType)
        )
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

    private Authentication getNonExistentUserAuthentication () {
        UserDetails principal = new User("non-existent@gmail.com", "anne123", [])
        return new UsernamePasswordAuthenticationToken(principal, null)
    }
}
