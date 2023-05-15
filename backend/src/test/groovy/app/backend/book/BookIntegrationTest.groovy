package app.backend.book

import app.backend.IntegrationTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import spock.lang.Unroll

import static app.backend.book.ConstraintViolationVariables.NEGATIVE_NUMBER
import static app.backend.book.ConstraintViolationVariables.WHITESPACE_STRING
import static app.backend.book.ConstraintViolationVariables.YEAR_GREATER_THAN_MAX
import static app.backend.book.ConstraintViolationVariables.YEAR_LESS_THAN_MIN
import static groovy.json.JsonOutput.toJson
import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
class BookIntegrationTest extends IntegrationTestConfig {
    @Autowired
    MockMvc mvc

    @WithMockUser
    def "should get all books and return 200 status code"() {
        when:
        def result = mvc
                .perform(get("/books"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.totalPages').value(1))
        result.andExpect(jsonPath('$.books[*].id', containsInAnyOrder(1, 2, 3, 4)))
        result.andExpect(jsonPath('$.books[*].title', containsInAnyOrder( "Dracula", "The Higgler", "Murder!", "The Three Musketeers")))
        result.andExpect(jsonPath('$.books[*].author', containsInAnyOrder("Stoker Bram", "A.E Coppard", "Arnold Bennet", "Alexandre Dumas")))
        result.andExpect(jsonPath('$.books[*].publishYear', containsInAnyOrder(2010, 2016, 2005, 1980)))
        result.andExpect(jsonPath('$.books[*].canBeBorrow', containsInAnyOrder(true, false, true, true)))
        result.andExpect(jsonPath('$.books[*].availableAmount', containsInAnyOrder(5, 13, 15, 9)))
        result.andExpect(jsonPath('$.books[*].type.id', containsInAnyOrder(4, 5, 3, 1)))
        result.andExpect(jsonPath('$.books[*].type.name', containsInAnyOrder("Horror", "Romance", "Crime", "Adventure stories")))

    }

    def "should get all books available to borrow and return 200 status code"() {
        when:
        def result = mvc
                .perform(get("/books/to-borrow"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.totalPages').value(1))
        result.andExpect(jsonPath('$.books[*]', hasSize(3)))
        result.andExpect(jsonPath('$.books[*].id', containsInAnyOrder(1, 3, 4)))
        result.andExpect(jsonPath('$.books[*].title', containsInAnyOrder( "Dracula", "Murder!", "The Three Musketeers")))
        result.andExpect(jsonPath('$.books[*].author', containsInAnyOrder("Stoker Bram", "Arnold Bennet", "Alexandre Dumas")))
        result.andExpect(jsonPath('$.books[*].publishYear', containsInAnyOrder(2010, 2005, 1980)))
        result.andExpect(jsonPath('$.books[*].canBeBorrow', containsInAnyOrder(true, true, true)))
        result.andExpect(jsonPath('$.books[*].availableAmount', containsInAnyOrder(5, 15, 9)))
        result.andExpect(jsonPath('$.books[*].type.id', containsInAnyOrder(4, 3, 1)))
        result.andExpect(jsonPath('$.books[*].type.name', containsInAnyOrder("Horror", "Crime", "Adventure stories")))

    }

    @WithMockUser(roles = "EMPLOYEE")
    def "should get all book to remove and return 200 status code"() {
        when:
        def result = mvc
                .perform(get("/books/to-remove"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.totalPages').value(1))
        result.andExpect(jsonPath('$.books[*]', hasSize(1)))
        result.andExpect(jsonPath('$.books[0].id').value(2))
        result.andExpect(jsonPath('$.books[0].title').value("The Higgler"))
        result.andExpect(jsonPath('$.books[0].author').value("A.E Coppard"))
        result.andExpect(jsonPath('$.books[0].publishYear').value(2016))
        result.andExpect(jsonPath('$.books[0].canBeBorrow').value(false))
        result.andExpect(jsonPath('$.books[0].availableAmount').value(13))
        result.andExpect(jsonPath('$.books[0].type.id').value(5))
        result.andExpect(jsonPath('$.books[0].type.name').value("Romance"))

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
        int id = 2

        when:
        def result = mvc
                .perform(get("/books/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(2))
        result.andExpect(jsonPath('$.title').value("The Higgler"))
        result.andExpect(jsonPath('$.author').value("A.E Coppard"))
        result.andExpect(jsonPath('$.publishYear').value(2016))
        result.andExpect(jsonPath('$.canBeBorrow').value(false))
        result.andExpect(jsonPath('$.availableAmount').value(13))
        result.andExpect(jsonPath('$.type.id').value(5))
        result.andExpect(jsonPath('$.type.name').value("Romance"))

    }

    def "should not get book by id and return 404 status code" () {
        given:
        int id = 10

        when:
        def result = mvc
                .perform(get("/books/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isNotFound())
    }

    @WithMockUser
    def "should get all book by user id and return 200 status code"() {
        given:
        int id = 2

        when:
        def result = mvc
                .perform(get("/books/user/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.totalPages').value(1))
        result.andExpect(jsonPath('$.books[*]', hasSize(2)))
        result.andExpect(jsonPath('$.books[*].id', containsInAnyOrder(1, 3)))
        result.andExpect(jsonPath('$.books[*].title', containsInAnyOrder( "Dracula", "Murder!")))
        result.andExpect(jsonPath('$.books[*].author', containsInAnyOrder("Stoker Bram", "Arnold Bennet")))
        result.andExpect(jsonPath('$.books[*].publishYear', containsInAnyOrder(2010, 2005)))
        result.andExpect(jsonPath('$.books[*].canBeBorrow', containsInAnyOrder(true, true)))
        result.andExpect(jsonPath('$.books[*].availableAmount', containsInAnyOrder(5, 15)))
        result.andExpect(jsonPath('$.books[*].type.id', containsInAnyOrder(4, 3)))
        result.andExpect(jsonPath('$.books[*].type.name', containsInAnyOrder("Horror", "Crime")))

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
        when:
        def result = mvc
                .perform(get("/books/criteria")
                        .param("types", "Crime", "Horror")
                        .param("min", "2005")
                        .param("max", "2010"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.totalPages').value(1))
        result.andExpect(jsonPath('$.books[*]', hasSize(2)))
        result.andExpect(jsonPath('$.books[*].id', containsInAnyOrder(1, 3)))
        result.andExpect(jsonPath('$.books[*].title', containsInAnyOrder( "Dracula", "Murder!")))
        result.andExpect(jsonPath('$.books[*].author', containsInAnyOrder("Stoker Bram", "Arnold Bennet")))
        result.andExpect(jsonPath('$.books[*].publishYear', containsInAnyOrder(2010, 2005)))
        result.andExpect(jsonPath('$.books[*].canBeBorrow', containsInAnyOrder(true, true)))
        result.andExpect(jsonPath('$.books[*].availableAmount', containsInAnyOrder(5, 15)))
        result.andExpect(jsonPath('$.books[*].type.id', containsInAnyOrder(4, 3)))
        result.andExpect(jsonPath('$.books[*].type.name', containsInAnyOrder("Horror", "Crime")))

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
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.[*]', hasSize(5)))
        result.andExpect(jsonPath('$.[*].id', containsInAnyOrder(1, 2, 3, 4, 5)))
        result.andExpect(jsonPath('$.[*].name', containsInAnyOrder("Adventure stories", "Classics", "Crime", "Horror", "Romance")))
    }

    @Transactional
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
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(5))
        result.andExpect(jsonPath('$.title').value("The Grass is Always Greener"))
        result.andExpect(jsonPath('$.author').value("Jeffrey Archer"))
        result.andExpect(jsonPath('$.publishYear').value(2019))
        result.andExpect(jsonPath('$.canBeBorrow').value(true))
        result.andExpect(jsonPath('$.availableAmount').value(10))
        result.andExpect(jsonPath('$.type.id').value(2))
        result.andExpect(jsonPath('$.type.name').value("Classics"))

    }

    @Transactional
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

    @Transactional
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

    @Transactional
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

    @Transactional
    @WithMockUser(username = "anneS@gmail.com")
    def "should borrow book and return 200 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(post("/books/$id/borrow"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
    }

    @Transactional
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

    @Transactional
    @WithMockUser(username = "susanW@gmail.com")
    def "should not borrow book and return 409 status code"() {
        given: "id of already borrowed book by user"
        int id = 1

        when:
        def result = mvc
                .perform(post("/books/$id/borrow"))
                .andDo(print())

        then:
        result.andExpect(status().isConflict())
    }

    @Transactional
    @WithMockUser(username = "susanW@gmail.com")
    def "should return book with 200 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(post("/books/$id/return"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
    }

    @Transactional
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

    @Transactional
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
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.title').value("The Grass is Always Greener"))
        result.andExpect(jsonPath('$.author').value("Jeffrey Archer"))
        result.andExpect(jsonPath('$.publishYear').value(2019))
        result.andExpect(jsonPath('$.canBeBorrow').value(true))
        result.andExpect(jsonPath('$.availableAmount').value(10))
        result.andExpect(jsonPath('$.type.id').value(2))
        result.andExpect(jsonPath('$.type.name').value("Classics"))
    }

    @Transactional
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

    @Transactional
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

    @Transactional
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


    @Transactional
    @WithMockUser(roles = "EMPLOYEE")
    def "should delete book and return 204 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(delete("/books/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isNoContent())
    }

    @Transactional
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

    @Transactional
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

    private LinkedHashMap<String, Serializable> getRequestBookWithoutId() {
        return [
                title          : "The Grass is Always Greener",
                author         : "Jeffrey Archer",
                publishYear    : 2019,
                canBeBorrow    : true,
                availableAmount: 10,
                type           : [
                        id  : 2,
                        name: "Classics"
                ]
        ]
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
                        id  : 2,
                        name: "Classics"
                ]
        ]
    }
}
