package app.backend.user

import groovy.json.JsonOutput
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

import static groovy.json.JsonOutput.toJson
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

//@AutoConfigureMockMvc
@WebMvcTest
class UserControllerTest extends Specification {

    @Autowired
    MockMvc mvc

    @SpringBean
    UserService userService = Mock()

    UserController userController

    def setup() {
        userController = new UserController(userService)
    }

    def "should get user by id"() {
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

    def "should login user by login data"() {
        given:
        def request = [
                email: "anne@gmail.com",
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
    }

    def "should throw MethodArgumentNotValidException when login data fail validation"() {
        given:
        LoginDTO loginData = new LoginDTO(null, "abc", false)

        when:
        userController.login(loginData)

        then:
        thrown(MethodArgumentNotValidException)

//        where:
//        email            | password  | employee
//        null             | "anne123" | false
//        " "              | "anne123" | false
//        "anne@gmail.com" | null      | false
//        "anne@gmail.com" | " "       | false
//        "anne@gmail.com" | "anne123" | null

    }

    private UserDTO getUserDTO() {
        return new UserDTO(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", false)
    }

    private UserEntity getUserEntity() {
        return new UserEntity(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", false)
    }

    private LoginDTO getLoginData() {
        new LoginDTO("anne@gmail.com", "anne123", false)
    }


}
