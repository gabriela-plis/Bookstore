package app.backend.user

import jakarta.persistence.EntityNotFoundException
import org.mapstruct.factory.Mappers
import spock.lang.Specification

class UserServiceTest extends Specification {

    UserRepository userRepository = Mock()

    UserMapper userMapper = Mappers.getMapper(UserMapper)

    UserService userService

    def setup() {
        userService = new UserService(userRepository, userMapper)
    }

    def "should find user by id"() {
        given:
        Integer id = 1
        1 * userRepository.findById(id) >> Optional.of(getUserEntity())

        when:
        UserDTO result = userService.findById(id)

        then:
        result == getUserDTO()
    }

    def "should throw EntityNotFoundException when user was not found by id"() {
        given:
        Integer id = 1
        1 * userRepository.findById(id) >> Optional.empty()

        when:
        userService.findById(id)

        then:
        thrown(EntityNotFoundException)
    }

    def "should find user by login data"() {
        given:
        LoginDTO loginData = getLoginData()
        1 * userRepository.findByEmailAndPasswordAndEmployee(loginData.email(), loginData.password(), loginData.employee()) >> Optional.of(getUserEntity())

        when:
        UserDTO result = userService.findByLoginData(loginData)

        then:
        result == getUserDTO()
    }

    def "should throw EntityNotFoundException when user was not found by login data"() {
        given:
        LoginDTO loginData = getLoginData()
        1 * userRepository.findByEmailAndPasswordAndEmployee(loginData.email(), loginData.password(), loginData.employee()) >> Optional.empty()

        when:
        userService.findByLoginData(loginData)

        then:
        thrown(EntityNotFoundException)
    }

    def "should update user"() {
        given:
        Integer id = 1
        UserDTO user = new UserDTO(1, "Anne", "Bellatrix", "123456789", "anne@gmail.com", "anne123", false)

        1 * userRepository.findById(id) >> Optional.of(getUserEntity())

        when:
        UserDTO result = userService.update(id, user)

        then:

        result == new UserDTO(1, "Anne", "Bellatrix", "123456789", "anne@gmail.com", "anne123", false)
    }

    def "should throw EntityNotFoundException when updated user doesn't exist in database"() {
        given:
        int id = 1
        UserDTO user = getUserDTO()
        1 * userRepository.findById(id) >> Optional.empty()

        when:
        userService.update(id, user)

        then:
        thrown(EntityNotFoundException)

    }

    def "should register user"() {
        given:
        UserDTO user = new UserDTO(id, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", false)
        userRepository.save(new UserEntity(id, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", false)) >> getUserEntity()

        when:
        UserDTO result = userService.register(user)

        then:
        result == getUserDTO()

        where:
        id << [0, null]
    }

    def "should throw IllegalArgumentException when user has id > 0"() {
        given:
        UserDTO user = getUserDTO()
        userRepository.save(getUserEntity()) >> getUserEntity()

        when:
        userService.register(user)

        then:
        thrown(IllegalArgumentException)
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

//    @Unroll
//    def "should find user by id #description"() {
//        when:
//            def result = userService.findById(userId)
//
//        then:
//            1 * userRepository.findById(userId) >> Optional.of(new UserEntity(id: userId))
//
//        and:
//            with(result) {
//                id == userId
//                firstName == null
//                lastName == null
//                phone == null
//                email == null
//                password == null
//                employee == null
//            }
//
//        where:
//            userId | description
//            1      | "where user id is 1"
//            2      | "where user id is 2"
//            3      | "where user id is 3"
//            4      | "where user id is 4"
//    }
}
