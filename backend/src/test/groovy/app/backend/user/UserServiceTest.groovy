package app.backend.user

import jakarta.persistence.EntityNotFoundException
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Unroll

class UserServiceTest extends Specification {

    UserRepository userRepository = Mock()

    UserMapper userMapper = Mappers.getMapper(UserMapper)

    UserService userService;

    def setup() {
        userService = new UserService(userRepository, userMapper)
    }

    def "should find user by id"() {
        given:
        int id = 1
        1 * userRepository.findById(id) >> Optional.of(new UserEntity(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", false))

        when:
        UserDTO result = userService.findById(id)

        then:
        result == new UserDTO(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", false)
    }

    def "should throw EntityNotFoundException when user was not found by id" () {
        given:
        int id = 1
        1 * userRepository.findById(id) >> Optional.empty()

        when:
        userService.findById(id)

        then:
        thrown(EntityNotFoundException)
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
