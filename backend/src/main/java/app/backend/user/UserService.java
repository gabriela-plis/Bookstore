package app.backend.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;

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
    public UserDTO update(int id, UserDTO updatedUser) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        mapper.updateEntity(userEntity, updatedUser);

        return mapper.toDTO(userEntity);
    }

    @Transactional
    public UserDTO register(RegisteredUserDTO user) {
        UserEntity userEntity = mapper.toEntity(user);

        RoleEntity role = roleRepository.findByName("CUSTOMER").orElseThrow(EntityNotFoundException::new);
        userEntity.getRoles().add(role);

        userRepository.save(userEntity);

        return mapper.toDTO(userEntity);
    }

}
