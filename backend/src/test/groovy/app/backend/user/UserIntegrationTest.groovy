package app.backend.user

import app.backend.IntegrationTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import spock.lang.Unroll

import static groovy.json.JsonOutput.toJson
import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
class UserIntegrationTest extends IntegrationTestConfig {
    @Autowired
    MockMvc mvc

    @WithMockUser
    def "should get all users return 200 status code"() {
        when:
        def result =  mvc
                .perform(get("/user/all"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$[*].id', containsInAnyOrder(1, 2, 3)))
        result.andExpect(jsonPath('$[*].firstName', containsInAnyOrder("Susan", "Horace", "Anne")))
        result.andExpect(jsonPath('$[*].lastName', containsInAnyOrder("Wilner", "Williams", "Smith")))
        result.andExpect(jsonPath('$[*].email', containsInAnyOrder("susanW@gmail.com", "horaceW@gmail.com", "anneS@gmail.com")))
        result.andExpect(jsonPath('$[*].phone', containsInAnyOrder(null, null, "576815233")))
        result.andExpect(jsonPath('$[*].roles', containsInAnyOrder(List.of("EMPLOYEE", "CUSTOMER"), List.of("CUSTOMER"), List.of("CUSTOMER"))))

    }

    @WithAnonymousUser
    def "should not get all users and return 401 status code"() {
        when: "trying access as non-authenticated user"
        def result =  mvc
                .perform(get("/user/all"))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())
    }

    @WithMockUser
    def "should get user by id and return 200 status code"() {
        given:
        int id = 3

        when:
        def result = mvc
                .perform(get("/user/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(3))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("576815233"))
        result.andExpect(jsonPath('$.email').value("anneS@gmail.com"))
        result.andExpect(jsonPath('$.roles[*]', hasSize(2)))
        result.andExpect(jsonPath('$.roles[0]').value("EMPLOYEE"))
        result.andExpect(jsonPath('$.roles[1]').value("CUSTOMER"))
    }

    @WithMockUser
    def "should not get user by id and return 404 status code"() {
        given: "user id that doesn't exist"
        int id = 100

        when:
        def result = mvc
                .perform(get("/user/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isNotFound())

    }

    @WithAnonymousUser
    def "should not get user by id and return 401 status code"() {
        given:
        int id = 1

        when: "trying access as non-authenticated user"
        def result = mvc
                .perform(get("/user/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())

    }

    @WithMockUser(username = "anneS@gmail.com")
    def "should get logged user and return 200 status code"() {
        when:
        def result = mvc
                .perform(get("/user"))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(3))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("576815233"))
        result.andExpect(jsonPath('$.email').value("anneS@gmail.com"))
        result.andExpect(jsonPath('$.roles[*]', hasSize(2)))
        result.andExpect(jsonPath('$.roles[0]').value("EMPLOYEE"))
        result.andExpect(jsonPath('$.roles[1]').value("CUSTOMER"))

    }

    @WithAnonymousUser
    def "should register user and return 200 status code"() {
        given:
        def request = [
                firstName: "Anne",
                lastName : "Wick",
                phone    : "123456789",
                email    : "anneW@gmail.com",
                password: "anne123"
        ]

        when:
        def result = mvc
                .perform(post("/user/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(4))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Wick"))
        result.andExpect(jsonPath('$.phone').value("123456789"))
        result.andExpect(jsonPath('$.email').value("anneW@gmail.com"))
        result.andExpect(jsonPath('$.roles[*]', hasSize(1)))
        result.andExpect(jsonPath('$.roles[0]').value("CUSTOMER"))
    }

    @WithAnonymousUser
    @Unroll
    def "should not register user when #invalidData fail validation and return 422 status code"() {
        given:
        def request = [
                firstName: firstName,
                lastName : lastName,
                phone    : "123456789",
                email    : email,
                password : password,
        ]

        when:
        def result = mvc
                .perform(post("/user/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isUnprocessableEntity())

        where:
        firstName | lastName | email            | password  | invalidData
        null      | "Smith"  | "anne@gmail.com" | "anne123" | "missing first name"
        " "       | "Smith"  | "anne@gmail.com" | "anne123" | "empty first name"
        "Anne"    | null     | "anne@gmail.com" | "anne123" | "missing last name"
        "Anne"    | " "      | "anne@gmail.com" | "anne123" | "empty last name"
        "Anne"    | "Smith"  | null             | "anne123" | "missing email"
        "Anne"    | "Smith"  | " "              | "anne123" | "empty email"
        "Anne"    | "Smith"  | "anne@gmail.com" | null      | "missing password"
        "Anne"    | "Smith"  | "anne@gmail.com" | " "       | "empty password"
    }

    @Transactional
    @WithMockUser(username = "anneS@gmail.com")
    def "should update user and return 200 status code"() {
        given: "changed firstName, phone, email"
        def request = [
                id       : 3,
                firstName: "Gabriela",
                lastName : "Smith",
                phone    : "123456789",
                email    : "anne@gmail.com",
                roles    : List.of("EMPLOYEE", "CUSTOMER")
        ]

        when:
        def result = mvc
                .perform(put("/user")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(3))
        result.andExpect(jsonPath('$.firstName').value("Gabriela"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("123456789"))
        result.andExpect(jsonPath('$.email').value("anne@gmail.com"))
        result.andExpect(jsonPath('$.roles[*]', hasSize(2)))
        result.andExpect(jsonPath('$.roles[0]').value("EMPLOYEE"))
        result.andExpect(jsonPath('$.roles[1]').value("CUSTOMER"))

    }

    @Transactional
    @WithMockUser
    @Unroll
    def "should not update user when #invalidData fail validation and return 422 status code"() {
        given:
        def request = [
                id       : 1,
                firstName: firstName,
                lastName : lastName,
                phone    : "123456789",
                email    : email,
        ]

        when:
        def result = mvc
                .perform(put("/user")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isUnprocessableEntity())

        where:

        firstName | lastName | email            | password  | invalidData
        null      | "Smith"  | "anne@gmail.com" | "anne123" | "missing first name"
        " "       | "Smith"  | "anne@gmail.com" | "anne123" | "empty first name"
        "Anne"    | null     | "anne@gmail.com" | "anne123" | "missing last name"
        "Anne"    | " "      | "anne@gmail.com" | "anne123" | "empty last name"
        "Anne"    | "Smith"  | null             | "anne123" | "missing email"
        "Anne"    | "Smith"  | " "              | "anne123" | "empty email"

    }

    @Transactional
    @WithMockUser(username = "anneS@gmail.com")
    def "should reset password and return 200 status code"() {
        given:
        def request = [
                currentPassword: "anne123",
                newPassword: "anne321"
        ]

        when:
        def result = mvc
                .perform(put("/user/password")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isOk())

    }

    @Transactional
    @WithMockUser(username = "anneS@gmail.com")
    def "should not reset password and return 500 status code"() {
        given:
        def request = [
                currentPassword: "abc",
                newPassword: "anne321"
        ]

        when:
        def result = mvc
                .perform(put("/user/password")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isBadRequest())

    }

    @Transactional
    @WithAnonymousUser
    def "should not reset password and return 401 status code"() {
        given:
        int id = 1

        when: "trying access as non-authenticated user"
        def result = mvc
                .perform(get("/user/$id"))
                .andDo(print())

        then:
        result.andExpect(status().isUnauthorized())

    }
}
