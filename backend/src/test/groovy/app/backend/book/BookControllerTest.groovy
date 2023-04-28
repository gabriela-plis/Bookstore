package app.backend.book

import app.backend.MvcSpecification
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import spock.lang.Unroll

import static app.backend.book.ConstraintViolationVariables.*
import static groovy.json.JsonOutput.*
import static org.hamcrest.Matchers.*
import static org.springframework.http.MediaType.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [BookController])
class BookControllerTest extends MvcSpecification {

    @SpringBean
    private final BookService bookService = Mock()

    @WithMockUser
    def "should get all books and return 200 status code"() {
        when:
        def result = mvc
                .perform(get("/books"))
                .andDo(print())

        then:
        1 * bookService.getAllBooks() >> getBookDTOs()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$[*].id', containsInAnyOrder(1, 2)))
        result.andExpect(jsonPath('$[*].title', containsInAnyOrder("The Grass is Always Greener", "The Higgler")))
        result.andExpect(jsonPath('$[*].author', containsInAnyOrder("Jeffrey Archer", "A.E. Coppard")))
        result.andExpect(jsonPath('$[*].publishYear', containsInAnyOrder(2019, 2016)))
        result.andExpect(jsonPath('$[*].canBeBorrow', containsInAnyOrder(true, true))) //poszukaj ladniejszego rozwiazania xd
        result.andExpect(jsonPath('$[*].availableAmount', containsInAnyOrder(10, 5)))
        result.andExpect(jsonPath('$[*].type.id', containsInAnyOrder(1, 1)))
        result.andExpect(jsonPath('$[*].type.name', containsInAnyOrder("Type", "Type")))

    }

    def "should get all books available to borrow and return 200 status code"() {
        when:
        def result = mvc
                .perform(get("/books/to-borrow"))
                .andDo(print())

        then:
        1 * bookService.getAllBooksToBorrow() >> getBookDTOs()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$[*].id', containsInAnyOrder(1, 2)))
        result.andExpect(jsonPath('$[*].title', containsInAnyOrder("The Grass is Always Greener", "The Higgler")))
        result.andExpect(jsonPath('$[*].author', containsInAnyOrder("Jeffrey Archer", "A.E. Coppard")))
        result.andExpect(jsonPath('$[*].publishYear', containsInAnyOrder(2019, 2016)))
        result.andExpect(jsonPath('$[*].canBeBorrow', containsInAnyOrder(true, true)))
        result.andExpect(jsonPath('$[*].availableAmount', containsInAnyOrder(10, 5)))
        result.andExpect(jsonPath('$[*].type.id', containsInAnyOrder(1, 1)))
        result.andExpect(jsonPath('$[*].type.name', containsInAnyOrder("Type", "Type")))

    }

    @WithMockUser(roles = "EMPLOYEE")
    def "should get all book to remove and return 200 status code"() {
        when:
        def result = mvc
                .perform(get("/books/to-remove"))
                .andDo(print())

        then:
        1 * bookService.getAllBooksToRemove() >> getBookDTOs() // czy moge

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$[*].id', containsInAnyOrder(1, 2)))
        result.andExpect(jsonPath('$[*].title', containsInAnyOrder("The Grass is Always Greener", "The Higgler")))
        result.andExpect(jsonPath('$[*].author', containsInAnyOrder("Jeffrey Archer", "A.E. Coppard")))
        result.andExpect(jsonPath('$[*].publishYear', containsInAnyOrder(2019, 2016)))
        result.andExpect(jsonPath('$[*].canBeBorrow', containsInAnyOrder(true, true)))
        result.andExpect(jsonPath('$[*].availableAmount', containsInAnyOrder(10, 5)))
        result.andExpect(jsonPath('$[*].type.id', containsInAnyOrder(1, 1)))
        result.andExpect(jsonPath('$[*].type.name', containsInAnyOrder("Type", "Type")))

    }

    @WithMockUser(roles = "CUSTOMER")
    def "should not get books to remove and return 403 status code"() {
        when: "trying access without needed privileges"
        def result = mvc
                .perform(get("/books/to-remove"))
                .andDo(print())

        then:
        result.andExpect(status().isForbidden())
    }

    @WithAnonymousUser
    def "should not get all books to remove and return 401 status code"() {
        when: "trying access as non-authenticated user"
        def result = mvc
                .perform(get("/books/to-remove"))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())
    }

    def "should get book by id"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(get("/books/$id"))
                .andDo(print())

        then:
        1 * bookService.getById(id) >> getBookDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.title').value("The Grass is Always Greener"))
        result.andExpect(jsonPath('$.author').value("Jeffrey Archer"))
        result.andExpect(jsonPath('$.publishYear').value(2019))
        result.andExpect(jsonPath('$.canBeBorrow').value(true))
        result.andExpect(jsonPath('$.availableAmount').value(10))
        result.andExpect(jsonPath('$.type.id').value(1))
        result.andExpect(jsonPath('$.type.name').value("Type"))
    }

    @WithMockUser
    def "should get all book by user email and return 200 status code"() {
        given:
        String email = "anne@gmail.com"

        when:
        def result = mvc
                .perform(get("/books/user/$email"))
                .andDo(print())

        then:
        1 * bookService.getByOwnerUser(email) >> getBookDTOs()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$[*].id', containsInAnyOrder(1, 2)))
        result.andExpect(jsonPath('$[*].title', containsInAnyOrder("The Grass is Always Greener", "The Higgler")))
        result.andExpect(jsonPath('$[*].author', containsInAnyOrder("Jeffrey Archer", "A.E. Coppard")))
        result.andExpect(jsonPath('$[*].publishYear', containsInAnyOrder(2019, 2016)))
        result.andExpect(jsonPath('$[*].canBeBorrow', containsInAnyOrder(true, true)))
        result.andExpect(jsonPath('$[*].availableAmount', containsInAnyOrder(10, 5)))
        result.andExpect(jsonPath('$[*].type.id', containsInAnyOrder(1, 1)))
        result.andExpect(jsonPath('$[*].type.name', containsInAnyOrder("Type", "Type")))

    }

    @WithAnonymousUser
    def "should not get books by user id and return 401 status code"() {
        given:
        int id = 1

        when: "trying access as non-authenticated user"
        def result = mvc
                .perform(get("/books/user/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())
    }

    def "should get all book by sorting criteria and return 200 status code"() {
        given:
        BookSortingCriteriaDTO criteria = new BookSortingCriteriaDTO(Set.of("Type"), 2016, 2019)

        when:
        def result = mvc
                .perform(get("/books/criteria")
                        .param("types", "Type")
                        .param("min", "2016")
                        .param("max", "2019"))
                .andDo(print())

        then:
        1 * bookService.getBySortingCriteria(criteria) >> getBookDTOs()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$[*].id', containsInAnyOrder(1, 2)))
        result.andExpect(jsonPath('$[*].title', containsInAnyOrder("The Grass is Always Greener", "The Higgler")))
        result.andExpect(jsonPath('$[*].author', containsInAnyOrder("Jeffrey Archer", "A.E. Coppard")))
        result.andExpect(jsonPath('$[*].publishYear', containsInAnyOrder(2019, 2016)))
        result.andExpect(jsonPath('$[*].canBeBorrow', containsInAnyOrder(true, true)))
        result.andExpect(jsonPath('$[*].availableAmount', containsInAnyOrder(10, 5)))
        result.andExpect(jsonPath('$[*].type.id', containsInAnyOrder(1, 1)))
        result.andExpect(jsonPath('$[*].type.name', containsInAnyOrder("Type", "Type")))

    }

    @Unroll
    def "should not get book by sorting criteria when #invalidData fail validation and return 422 status code"() {
        when:
        def result = mvc
                .perform(get("/books/criteria")
                        .param("types", type)
                        .param("min", minYear as String)
                        .param("max", maxYear as String))
                .andDo(print())

        then:
        result.andExpect(status().isUnprocessableEntity())

        where:
        type              | minYear               | maxYear               | invalidData
        "romance"         | YEAR_LESS_THAN_MIN    | 2015                  | "date < min"
        "romance"         | YEAR_GREATER_THAN_MAX | 2015                  | "date > max"
        "romance"         | null                  | 2015                  | "missing date"
        "romance"         | 2010                  | null                  | "missing max date"
        "romance"         | 2010                  | YEAR_LESS_THAN_MIN    | "date < min"
        "romance"         | 2010                  | YEAR_GREATER_THAN_MAX | "date > max"

    }

    def "should get book types"() {
        when:
        def result = mvc
                .perform(get("/books/types"))
                .andDo(print())

        then:
        1 * bookService.getAllBookTypes() >> List.of(
                new BookTypeDTO(1, "Crime"),
                new BookTypeDTO(2, "Romance")
        )

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.[*]', hasSize(2)))
        result.andExpect(jsonPath('$.[*].id', containsInAnyOrder(1, 2)))
        result.andExpect(jsonPath('$.[*].name', containsInAnyOrder("Crime", "Romance")))
    }

    @WithMockUser(roles = "EMPLOYEE")
    def "should add book and return 200 status code"() {
        given:
        def request = getRequestBookWithoutId()

        when:
        def result = mvc
                .perform(post("/books")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        1 * bookService.save(_ as BookDTO) >> getBookDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.title').value("The Grass is Always Greener"))
        result.andExpect(jsonPath('$.author').value("Jeffrey Archer"))
        result.andExpect(jsonPath('$.publishYear').value(2019))
        result.andExpect(jsonPath('$.canBeBorrow').value(true))
        result.andExpect(jsonPath('$.availableAmount').value(10))
        result.andExpect(jsonPath('$.type.id').value(1))
        result.andExpect(jsonPath('$.type.name').value("Type"))

    }

    @WithMockUser(roles = "EMPLOYEE")
    def "should not add book when #invalidData fail validation and return 422 status code"() {
        given:
        def request = [
                id             : id,
                title          : title,
                author         : author,
                publishYear    : publishYear,
                canBeBorrow    : canBeBorrow,
                availableAmount: availableAmount,
                type           : [
                        id  : typeId,
                        name: typeName
                ]
        ]

        when:
        def result = mvc
                .perform(post("/books")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isUnprocessableEntity())

        where:
        id | title                         | author            | publishYear           | canBeBorrow | availableAmount | typeId          | typeName          | invalidData
        5  | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | "Type"            | "id is present"
        1  | null                          | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | "Type"            | "missing title"
        1  | WHITESPACE_STRING             | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | "Type"            | "whitespace title"
        1  | "The Grass is Always Greener" | null              | 2019                  | true        | 10              | 1               | "Type"            | "missing author"
        1  | "The Grass is Always Greener" | WHITESPACE_STRING | 2019                  | true        | 10              | 1               | "Type"            | "whitespace author"
        1  | "The Grass is Always Greener" | "Jeffrey Archer"  | null                  | true        | 10              | 1               | "Type"            | "missing publish year"
        1  | "The Grass is Always Greener" | "Jeffrey Archer"  | YEAR_LESS_THAN_MIN    | true        | 10              | 1               | "Type"            | "publish year < min date"
        1  | "The Grass is Always Greener" | "Jeffrey Archer"  | YEAR_GREATER_THAN_MAX | true        | 10              | 1               | "Type"            | "publish year > max date"
        1  | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | null        | 10              | 1               | "Type"            | "missing can be borrow info"
        1  | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | NEGATIVE_NUMBER | 1               | "Type"            | "negative available amount"
        1  | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | 10              | NEGATIVE_NUMBER | "Type"            | "negative book type id"
        1  | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | null              | "missing book type name"
        1  | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | WHITESPACE_STRING | "whitespace book type name"

    }

    @WithAnonymousUser
    def "should not add book and return 401 status code"() {
        given:
        def request = getRequestBookWithoutId()

        when: "trying access as non-authenticated user"
        def result = mvc
                .perform(post("/books")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())
    }

    @WithMockUser
    def "should not add book and return 403 status code"() {
        given:
        def request = getRequestBookWithoutId()

        when: "trying access as user without privileges"
        def result = mvc
                .perform(post("/books")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isForbidden())
    }

    @WithMockUser
    def "should borrow book and return 200 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(post("/books/$id/borrow"))
                .andDo(print())

        then:
        1 * bookService.borrowBook(id)

        and:
        result.andExpect(status().isOk())
    }

    @WithAnonymousUser
    def "should not borrow book and return 401 status code"() {
        given:
        int id = 1

        when: "trying access as non-authenticated user"
        def result = mvc
                .perform(post("/books/$id/borrow"))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())
    }

    @WithMockUser
    def "should return book with 200 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(post("/books/$id/return"))
                .andDo(print())

        then:
        1 * bookService.returnBook(id)

        and:
        result.andExpect(status().isOk())
    }

    @WithAnonymousUser
    def "should not return book and return 401 status code"() {
        given:
        int id = 1

        when: "trying access as non-authenticated user"
        def result = mvc
                .perform(post("/books/$id/return"))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())
    }

    @WithMockUser(roles = "EMPLOYEE")
    def "should update book and return 200 status code"() {
        given:
        def request = getRequestBook()

        when:
        def result = mvc
                .perform(put("/books")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        1 * bookService.update(_ as BookDTO) >> getBookDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.title').value("The Grass is Always Greener"))
        result.andExpect(jsonPath('$.author').value("Jeffrey Archer"))
        result.andExpect(jsonPath('$.publishYear').value(2019))
        result.andExpect(jsonPath('$.canBeBorrow').value(true))
        result.andExpect(jsonPath('$.availableAmount').value(10))
        result.andExpect(jsonPath('$.type.id').value(1))
        result.andExpect(jsonPath('$.type.name').value("Type"))
    }

    @WithMockUser(roles = "EMPLOYEE")
    def "should not update book when #invalidData fail validation and return 422 status code"() {
        given:
        def request = [
                id             : id,
                title          : title,
                author         : author,
                publishYear    : publishYear,
                canBeBorrow    : canBeBorrow,
                availableAmount: availableAmount,
                type           : [
                        id  : typeId,
                        name: typeName
                ]
        ]

        when:
        def result = mvc
                .perform(put("/books")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isUnprocessableEntity())

        where:
        id              | title                         | author            | publishYear           | canBeBorrow | availableAmount | typeId          | typeName          | invalidData
        null            | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | "Type"            | "missing id"
        NEGATIVE_NUMBER | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | "Type"            | "negative id"
        1               | null                          | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | "Type"            | "missing title"
        1               | WHITESPACE_STRING             | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | "Type"            | "whitespace title"
        1               | "The Grass is Always Greener" | null              | 2019                  | true        | 10              | 1               | "Type"            | "missing author"
        1               | "The Grass is Always Greener" | WHITESPACE_STRING | 2019                  | true        | 10              | 1               | "Type"            | "whitespace author"
        1               | "The Grass is Always Greener" | "Jeffrey Archer"  | null                  | true        | 10              | 1               | "Type"            | "missing publish year"
        1               | "The Grass is Always Greener" | "Jeffrey Archer"  | YEAR_LESS_THAN_MIN    | true        | 10              | 1               | "Type"            | "publish year < min date"
        1               | "The Grass is Always Greener" | "Jeffrey Archer"  | YEAR_GREATER_THAN_MAX | true        | 10              | 1               | "Type"            | "publish year > max date"
        1               | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | null        | 10              | 1               | "Type"            | "missing can be borrow info"
        1               | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | NEGATIVE_NUMBER | 1               | "Type"            | "negative available amount"
        1               | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | 10              | NEGATIVE_NUMBER | "Type"            | "negative book type id"
        1               | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | null              | "missing book type name"
        1               | "The Grass is Always Greener" | "Jeffrey Archer"  | 2019                  | true        | 10              | 1               | WHITESPACE_STRING | "whitespace book type name"


    }

    @WithAnonymousUser
    def "should not update book and return 401 status code"() {
        given:
        def request = getRequestBook()

        when: "trying access as non-authenticated user"
        def result = mvc
                .perform(put("/books")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())
    }

    @WithMockUser
    def "should not update book and return 403 status code"() {
        given:
        def request = getRequestBook()

        when: "trying access as user without privileges"
        def result = mvc
                .perform(put("/books")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isForbidden())
    }

    @WithMockUser(roles = "EMPLOYEE")
    def "should delete book and return 204 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(delete("/books/$id"))
                .andDo(print())

        then:
        1 * bookService.delete(id)

        and:
        result.andExpect(status().isNoContent())
    }

    @WithAnonymousUser
    def "should not delete book and return 401 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(delete("/books/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())
    }

    @WithMockUser
    def "should not delete book and return 403 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(delete("/books/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isForbidden())
    }

    private LinkedHashMap<String, Serializable> getRequestBook() {
        return [
                id             : 1,
                title          : "The Grass is Always Greener",
                author         : "Jeffrey Archer",
                publishYear    : 2019,
                canBeBorrow    : true,
                availableAmount: 10,
                type           : [
                        id  : 1,
                        name: "Type"
                ]
        ]
    }

    private LinkedHashMap<String, Serializable> getRequestBookWithoutId() {
        return [
                title          : "The Grass is Always Greener",
                author         : "Jeffrey Archer",
                publishYear    : 2019,
                canBeBorrow    : true,
                availableAmount: 10,
                type           : [
                        id  : 1,
                        name: "Type"
                ]
        ]
    }

    private BookDTO getBookDTO() {
        BookTypeDTO type = new BookTypeDTO(1, "Type")

        return new BookDTO(1, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, type)
    }

    private List<BookDTO> getBookDTOs() {

        BookTypeDTO type = new BookTypeDTO(1, "Type")

        return List.of(
                new BookDTO(1, "The Grass is Always Greener", "Jeffrey Archer", 2019, true, 10, type),
                new BookDTO(2, "The Higgler", "A.E. Coppard", 2016, true, 5, type),
        )
    }

}
