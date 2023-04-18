package app.backend.user

import app.backend.book.BookEntity
import jakarta.persistence.EntityNotFoundException
import org.mapstruct.factory.Mappers
import spock.lang.Specification

class UserServiceTest extends Specification {

    UserRepository userRepository = Mock()

    UserMapper userMapper = Mappers.getMapper(UserMapper)

    RoleRepository roleRepository = Mock()

    UserService userService

    def setup() {
        userService = new UserService(userRepository, roleRepository, userMapper)
    }

    def "should get user by email"() {
        given:
        String email = "anne@gmail.com"

        1 * userRepository.findByEmail(email) >> Optional.of(getUserEntity())

        when:
        UserDTO result = userService.getByEmail(email)

        then:
        result == getUserDTO()

    }

    def "should throw EntityNotFoundException when user was not found by email"() {
        given:
        String email = "anne@gmail.com"

        1 * userRepository.findByEmail(email) >> Optional.empty()

        when:
        userService.getByEmail(email)

        then:
        thrown(EntityNotFoundException)
    }

    def "should get user by id"() {
        given:
        Integer id = 1
        1 * userRepository.findById(id) >> Optional.of(getUserEntity())

        when:
        UserDTO result = userService.getById(id)

        then:
        result == getUserDTO()
    }

    def "should throw EntityNotFoundException when user was not found by id"() {
        given:
        Integer id = 1
        1 * userRepository.findById(id) >> Optional.empty()

        when:
        userService.getById(id)

        then:
        thrown(EntityNotFoundException)
    }

    def "should update user"() {
        given:
        Integer id = 1
        UserDTO user = new UserDTO(1, "Anne", "Bellatrix", "123456789", "anne@gmail.com", "anne123", List.of("CUSTOMER"))

        1 * userRepository.findById(id) >> Optional.of(getUserEntity())

        when:
        UserDTO result = userService.update(id, user)

        then:

        result == new UserDTO(1, "Anne", "Bellatrix", "123456789", "anne@gmail.com", "anne123", List.of("CUSTOMER"))
    }

    def "should throw EntityNotFoundException when updated user doesn't exists"() {
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
        RegisteredUserDTO user = getRegisteredUser()

        1 * userRepository.save(_ as UserEntity) >> getUserEntityWithoutRoles()

        1 * roleRepository.findByName(_) >> Optional.of(new RoleEntity(1, "CUSTOMER", new ArrayList<UserEntity>()))

        when:
        UserDTO result = userService.register(user)

        then:
        result == getUserDTO()
    }

    def "should throw EntityNotFound while register when role was not found"() {
        given:
        RegisteredUserDTO user = getRegisteredUser()

        1 * userRepository.save(_ as UserEntity) >> getUserEntityWithoutRoles()

        1 * roleRepository.findByName(_) >> Optional.empty()

        when:
        userService.register(user)

        then:
        thrown(EntityNotFoundException)
    }

    private UserDTO getUserDTO() {
        List<String> roles = List.of(
                "CUSTOMER"
        )


        return new UserDTO(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", roles)
    }

    private UserEntity getUserEntity() {
        List <RoleEntity> roles = new ArrayList<>()
        roles.add(new RoleEntity(1, "CUSTOMER", new ArrayList<UserEntity>()))

        return new UserEntity(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", roles, new ArrayList<BookEntity>())
    }

    private UserEntity getUserEntityWithoutRoles() {
        return new UserEntity(1, "Anne", "Smith", "123456789", "anne@gmail.com", "anne123", new ArrayList<RoleEntity>(), new ArrayList<BookEntity>())
    }

    private RegisteredUserDTO getRegisteredUser() {
        return new RegisteredUserDTO("Anne", "Smith", "123456789", "anne@gmail.com", "anne123")
    }

}
