package app.backend.user

import app.backend.MvcSpecification
import jakarta.persistence.EntityNotFoundException
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser

import spock.lang.Unroll

import static groovy.json.JsonOutput.toJson
import static org.hamcrest.Matchers.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@WebMvcTest(controllers = [UserController])
class UserControllerTest extends MvcSpecification {

    @SpringBean
    private final UserService userService = Mock()

    @WithMockUser
    def "should get all users return 200 status code"() {
        when:
        def result =  mvc
                .perform(get("/user/all"))
                .andDo(print())

        then:
        1 * userService.getAllUsers() >> getUserDTOs()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$[*].id', containsInAnyOrder(1, 2)))
        result.andExpect(jsonPath('$[*].firstName', containsInAnyOrder("Anne", "Ann")))
        result.andExpect(jsonPath('$[*].lastName', containsInAnyOrder("Smith", "Johnson")))
        result.andExpect(jsonPath('$[*].email', containsInAnyOrder("anneS@gmail.com", "annJ@gmail.com")))
        result.andExpect(jsonPath('$[*].phone', containsInAnyOrder("123456789", "987654321")))
        result.andExpect(jsonPath('$[*].roles', containsInAnyOrder(List.of("CUSTOMER", "EMPLOYEE"), List.of("CUSTOMER"))))

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
        int id = 1

        when:
        def result = mvc
                .perform(get("/user/$id"))
                .andDo(print())

        then:
        1 * userService.getById(id) >> getUserDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("123456789"))
        result.andExpect(jsonPath('$.email').value("anne@gmail.com"))

    }

    @WithMockUser
    def "should not get user by id and return 404 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(get("/user/$id"))
                .andDo(print())

        then:
        1 * userService.getById(id) >> { throw new EntityNotFoundException() }

        and:
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

    @WithMockUser(username = "anne@gmail.com")
    def "should get logged user and return 200 status code"() {
        given:
        String email = "anne@gmail.com"

        when:
        def result = mvc
                .perform(get("/user"))
                .andDo(print())

        then:
        1 * userService.getByEmail(email) >> getUserDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("123456789"))
        result.andExpect(jsonPath('$.email').value("anne@gmail.com"))
        result.andExpect(jsonPath('$.roles[*]', hasSize(1)))
        result.andExpect(jsonPath('$.roles[0]').value("CUSTOMER"))

    }

    @WithAnonymousUser
    def "should register user and return 200 status code"() {
        given:
        def request = [
                firstName: "Anne",
                lastName : "Smith",
                phone    : "123456789",
                email    : "anne@gmail.com",
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
        1 * userService.register(_ as RegisteredUserDTO) >> getUserDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("123456789"))
        result.andExpect(jsonPath('$.email').value("anne@gmail.com"))
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

    @WithMockUser(username = "anne123@gmail.com")
    def "should update user and return 200 status code"() {
        given:
        def request = [
                id       : 1,
                firstName: "Anne",
                lastName : "Smith",
                phone    : "123456789",
                email    : "anne@gmail.com",
                roles    : List.of("CUSTOMER")
        ]

        String email = "anne123@gmail.com"

        when:
        def result = mvc
                .perform(put("/user")
                .contentType(APPLICATION_JSON)
                .content(toJson(request))
                .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        1 * userService.update(_ as UserDTO, email) >> getUserDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("123456789"))
        result.andExpect(jsonPath('$.email').value("anne@gmail.com"))
        result.andExpect(jsonPath('$.roles[*]', hasSize(1)))
        result.andExpect(jsonPath('$.roles[0]').value("CUSTOMER"))

    }

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

    @WithMockUser(username = "anne@gmail.com")
    def "should reset password and return 200 status code"() {
        given:
        def request = [
                currentPassword: "anne123",
                newPassword: "anne321"
        ]

        String email = "anne@gmail.com"

        when:
        def result = mvc
                .perform(put("/user/password")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        1 * userService.resetPassword(_ as ResetPasswordDTO, email)

        and:
        result.andExpect(status().isOk())

    }

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


    private UserDTO getUserDTO() {
        return new UserDTO(1, "Anne", "Smith", "123456789", "anne@gmail.com", List.of("CUSTOMER"))
    }

    private List<UserDTO> getUserDTOs() {
        return List.of(
                new UserDTO(1, "Anne", "Smith", "123456789", "anneS@gmail.com", List.of("CUSTOMER", "EMPLOYEE")),
                new UserDTO(2, "Ann", "Johnson", "987654321", "annJ@gmail.com", List.of("CUSTOMER"))
        )
    }

}
