package app.backend.user;

import app.backend.utils.exceptions.WrongPasswordException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;

    public UserDTO getByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDTO(userEntity);
    }

    public UserDTO getById(int id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDTO(userEntity);
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        return mapper.toDTOs(userEntities);
    }

    @Transactional
    public UserDTO update(UserDTO updatedUser, String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        mapper.updateEntity(userEntity, updatedUser);

        return mapper.toDTO(userEntity);
    }

    @Transactional
    public void resetPassword(ResetPasswordDTO passwords, String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        if (encoder.matches(passwords.currentPassword(), userEntity.getPassword())) {
            userEntity.setPassword(encoder.encode(passwords.newPassword()));
        } else {
            throw new WrongPasswordException();
        }

    }

    @Transactional
    public UserDTO register(RegisteredUserDTO user) {
        UserEntity userEntity = mapper.toEntity(user);

        userEntity.setPassword(encoder.encode(user.password()));

        RoleEntity role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(EntityNotFoundException::new);
        userEntity.getRoles().add(role);

        UserEntity savedUser = userRepository.save(userEntity);

        return mapper.toDTO(savedUser);
    }

}
