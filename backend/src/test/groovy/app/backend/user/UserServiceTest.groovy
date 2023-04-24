package app.backend.user

import app.backend.book.BookEntity
import app.backend.utils.exceptions.WrongPasswordException
import jakarta.persistence.EntityNotFoundException
import org.mapstruct.factory.Mappers
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

class UserServiceTest extends Specification {

    UserRepository userRepository = Mock()

    UserMapper userMapper = Mappers.getMapper(UserMapper)

    RoleRepository roleRepository = Mock()

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder()

    UserService userService

    def setup() {
        userService = new UserService(userRepository, roleRepository, userMapper, encoder)
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

    def "should get all users"() {
        given:
        1 * userRepository.findAll() >> getUserEntities()

        when:
        List<UserDTO> result = userService.getAllUsers()

        then:
        result == getUserDTOs()

    }

    def "should update user"() {
        given:
        String email = "anne@gmail.com"
        UserDTO user = new UserDTO(1, "Anne", "Bellatrix", "123456789", "anne@gmail.com", List.of("CUSTOMER"))

        1 * userRepository.findByEmail(email) >> Optional.of(getUserEntity())

        when:
        UserDTO result = userService.update(user, email)

        then:

        result == new UserDTO(1, "Anne", "Bellatrix", "123456789", "anne@gmail.com", List.of("CUSTOMER"))
    }

    def "should throw EntityNotFoundException when updated user doesn't exists"() {
        given:
        String email = "anne@gmail.com"
        UserDTO user = getUserDTO()
        1 * userRepository.findByEmail(email) >> Optional.empty()

        when:
        userService.update(user, email)

        then:
        thrown(EntityNotFoundException)

    }

    def "should reset password"() {
        given:
        String email = "anne@gmail.com"
        ResetPasswordDTO passwords = new ResetPasswordDTO("anne123", "anne321")

        when:
        userService.resetPassword(passwords, email)

        then:
        1 * userRepository.findByEmail(email) >> Optional.of(getUserEntity())

    }

    def "should thrown EntityNotFoundException when user trying to reset password doesn't exists"() {
        given:
        String email = "anne@gmail.com"
        ResetPasswordDTO passwords = new ResetPasswordDTO("anne123", "anne321")

        1 * userRepository.findByEmail(email) >> Optional.empty()

        when:
        userService.resetPassword(passwords, email)

        then:
        thrown(EntityNotFoundException)

    }

    def "should thrown EntityNotFoundException when given password and password in database doesn't match"() {
        given:
        String email = "anne@gmail.com"
        ResetPasswordDTO passwords = new ResetPasswordDTO("anne333", "anne321")

        1 * userRepository.findByEmail(email) >> Optional.of(getUserEntity())

        when:
        userService.resetPassword(passwords, email)

        then:
        thrown(WrongPasswordException)

    }

    def "should register user"() {
        given:
        RegisteredUserDTO user = getRegisteredUser()

        1 * userRepository.save(_ as UserEntity) >> getUserEntity()

        1 * roleRepository.findByName(_) >> Optional.of(new RoleEntity(1, "CUSTOMER", new ArrayList<UserEntity>()))

        when:
        UserDTO result = userService.register(user)

        then:
        result == getUserDTO()
    }

    def "should throw EntityNotFound while register when role was not found"() {
        given:
        RegisteredUserDTO user = getRegisteredUser()
        getUserEntity().setRoles(new ArrayList<RoleEntity>())

        1 * roleRepository.findByName(_) >> Optional.empty()

        when:
        userService.register(user)

        then:
        thrown(EntityNotFoundException)
    }

    private UserDTO getUserDTO() {
        List<String> roles = new ArrayList<>()
        roles.add("CUSTOMER")

        return new UserDTO(1, "Anne", "Smith", "123456789", "anne@gmail.com", roles)
    }

    private List<UserDTO> getUserDTOs() {
        return List.of(
                new UserDTO(1, "Anne", "Smith", "123456789", "anneS@gmail.com", List.of("CUSTOMER", "EMPLOYEE")),
                new UserDTO(2, "Ann", "Johnson", "987654321", "annJ@gmail.com", List.of("CUSTOMER"))
        )
    }

    private UserEntity getUserEntity() {
        List <RoleEntity> roles = new ArrayList<>()
        roles.add(new RoleEntity(1, "CUSTOMER", new ArrayList<UserEntity>()))

        return new UserEntity(1, "Anne", "Smith", "123456789", "anne@gmail.com", "\$2a\$10\$RH3zmSYJx.oebodivDoFV.O./KOjxtVK9uzmjSC4ubeNJZk.hfpfC", roles, new ArrayList<BookEntity>())
    }

    private List<UserEntity> getUserEntities() {
        List<RoleEntity> anneRoles = List.of(
                new RoleEntity(1, "CUSTOMER", null),
                new RoleEntity(1, "EMPLOYEE", null),
        )

        List<RoleEntity> annRoles = List.of(
                new RoleEntity(1, "CUSTOMER", null),
        )

        return List.of(
                new UserEntity(1, "Anne", "Smith", "123456789", "anneS@gmail.com", "anne123",anneRoles ,new ArrayList<BookEntity>()),
                new UserEntity(2, "Ann", "Johnson", "987654321", "annJ@gmail.com", "anne321",annRoles ,new ArrayList<BookEntity>())
        )
    }


    private RegisteredUserDTO getRegisteredUser() {
        return new RegisteredUserDTO("Anne", "Smith", "123456789", "anne@gmail.com", "anne123")
    }

}
