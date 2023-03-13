package app.backend.user

import groovy.json.JsonOutput
import jakarta.persistence.EntityNotFoundException
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.web.bind.MethodArgumentNotValidException
import spock.lang.Specification
import spock.lang.Unroll

import static groovy.json.JsonOutput.toJson
import static org.springframework.http.MediaType.APPLICATION_ATOM_XML
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

//@AutoConfigureMockMvc
@WebMvcTest(controllers = [UserController])
class UserControllerTest extends Specification {

    @Autowired
    MockMvc mvc

    @SpringBean
    UserService userService = Mock()

    UserController userController

    def setup() {
        userController = new UserController(userService)
    }

    def "should get user by id and return 200 status code"() {
        given:
        int id = 1

        when:
        def result = mvc
                .perform(get("/users/$id"))
                .andDo(print())

        then:
        1 * userService.findById(id) >> getUserDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("123456789"))
        result.andExpect(jsonPath('$.email').value("anne@gmail.com"))
        result.andExpect(jsonPath('$.password').value("anne123"))
        result.andExpect(jsonPath('$.employee').value(false))
    }

    def "should not find user by id and return 404 status code"() {
        given:
        int id = 1

        when:
        def result = mvc.perform(get("/users/$id"))
                .andDo(print())

        then:
        1 * userService.findById(id) >> { throw new EntityNotFoundException() }

        and:
        result.andExpect(status().isNotFound())
    }

    def "should login user by login data and return 200 status code"() {
        given:
        def request = [
                email   : "anne@gmail.com",
                password: "anne123",
                employee: false
        ]

        when:
        def result = mvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(toJson(request))
                .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        1 * userService.findByLoginData(_ as LoginDTO) >> getUserDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("123456789"))
        result.andExpect(jsonPath('$.email').value("anne@gmail.com"))
        result.andExpect(jsonPath('$.password').value("anne123"))
        result.andExpect(jsonPath('$.employee').value(false))

//        result.andExpectAll(
//            status().isOk(),
//            jsonPath('$.id').value(1),
//            jsonPath('$.firstName').value("Anne"),
//            jsonPath('$.lastName').value("Smith"),
//            jsonPath('$.phone').value("123456789"),
//            jsonPath('$.email').value("anne@gmail.com"),
//            jsonPath('$.password').value("anne123"),
//            jsonPath('$.employee').value(false)
//        )
    }

    @Unroll
    def "should not login when #invalidData fail validation and return 422 status code"() {
        given:
        def request = [
                email   : email,
                password: password,
                employee: employee
        ]

        when:
        def result = mvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(toJson(request))
                .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isUnprocessableEntity())

        where:
        email            | password  | employee | invalidData
        null             | "anne123" | false    | "missing email"
        " "              | "anne123" | false    | "empty email"
        "anne@gmail.com" | null      | false    | "missing password"
        "anne@gmail.com" | " "       | false    | "empty password"
        "anne@gmail.com" | "anne123" | null     | "missing is employee"

    }

    def "should register user and return 200 status code"() {
        given:
        def request = [
                firstName: "Anne",
                lastName : "Smith",
                phone    : "123456789",
                email    : "anne@gmail.com",
                password : "anne123",
                employee : false
        ]

        when:
        def result = mvc.perform(post("/users/register")
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
        result.andExpect(jsonPath('$.password').value("anne123"))
        result.andExpect(jsonPath('$.employee').value(false))
    }

    @Unroll
    def "should not register user when #invalidData fail validation and return 422 status code"() {
        given:
        def request = [
                firstName: firstName,
                lastName : lastName,
                phone    : "123456789",
                email    : email,
                password : password,
                employee : employee
        ]

        when:
        def result = mvc.perform(post("/users/register")
                .contentType(APPLICATION_JSON)
                .content(toJson(request))
                .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isUnprocessableEntity())

        where:
        firstName | lastName | email            | password  | employee | invalidData
        null      | "Smith"  | "anne@gmail.com" | "anne123" | false    | "missing first name"
        " "       | "Smith"  | "anne@gmail.com" | "anne123" | false    | "empty first name"
        "Anne"    | null     | "anne@gmail.com" | "anne123" | false    | "missing last name"
        "Anne"    | " "      | "anne@gmail.com" | "anne123" | false    | "empty last name"
        "Anne"    | "Smith"  | null             | "anne123" | false    | "missing email"
        "Anne"    | "Smith"  | " "              | "anne123" | false    | "empty email"
        "Anne"    | "Smith"  | "anne@gmail.com" | null      | false    | "missing password"
        "Anne"    | "Smith"  | "anne@gmail.com" | " "       | false    | "empty password"
        "Anne"    | "Smith"  | "anne@gmail.com" | "anne123" | null     | "missing is employee"
    }

    def "should update user and return 200 status code"() {
        given:
        int id = 1
        def request = [
                id       : id,
                firstName: "Anne",
                lastName : "Smith",
                phone    : "123456789",
                email    : "anne@gmail.com",
                password : "anne123",
                employee : false
        ]

        when:
        def result = mvc.perform(put("/users/$id")
                .contentType(APPLICATION_JSON)
                .content(toJson(request))
                .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        1 * userService.update(id, getUserDTO()) >> getUserDTO()

        and:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.id').value(1))
        result.andExpect(jsonPath('$.firstName').value("Anne"))
        result.andExpect(jsonPath('$.lastName').value("Smith"))
        result.andExpect(jsonPath('$.phone').value("123456789"))
        result.andExpect(jsonPath('$.email').value("anne@gmail.com"))
        result.andExpect(jsonPath('$.password').value("anne123"))
        result.andExpect(jsonPath('$.employee').value(false))
    }

    @Unroll
    def "should not update user when #invalidData fail validation and return 422 status code"() {
        given:
        int id = 1

        def request = [
                id       : id,
                firstName: firstName,
                lastName : lastName,
                phone    : "123456789",
                email    : email,
                password : password,
                employee : employee
        ]

        when:
        def result = mvc.perform(put("/users/$id")
                .contentType(APPLICATION_JSON)
                .content(toJson(request))
                .accept(APPLICATION_JSON))
                .andDo(print())

        then:
        result.andExpect(status().isUnprocessableEntity())

        where:
        firstName | lastName | email            | password  | employee | invalidData
        null      | "Smith"  | "anne@gmail.com" | "anne123" | false    | "missing first name"
        " "       | "Smith"  | "anne@gmail.com" | "anne123" | false    | "empty first name"
        "Anne"    | null     | "anne@gmail.com" | "anne123" | false    | "missing last name"
        "Anne"    | " "      | "anne@gmail.com" | "anne123" | false    | "empty last name"
        "Anne"    | "Smith"  | null             | "anne123" | false    | "missing email"
        "Anne"    | "Smith"  | " "              | "anne123" | false    | "empty email"
        "Anne"    | "Smith"  | "anne@gmail.com" | null      | false    | "missing password"
        "Anne"    | "Smith"  | "anne@gmail.com" | " "       | false    | "empty password"
        "Anne"    | "Smith"  | "anne@gmail.com" | "anne123" | null     | "missing is employee"
    }

    private UserDTO getUserDTO() {
        return new UserDTO(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", false)
    }

//    private UserEntity getUserEntity() {
//        return new UserEntity(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", false)
//    }
//
//    private LoginDTO getLoginData() {
//        new LoginDTO("anne@gmail.com", "anne123", false)
//    }


}
