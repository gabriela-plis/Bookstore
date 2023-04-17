package app.backend.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDTO getByEmail(String email) {
        UserEntity userEntity = repository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDTO(userEntity);
    }

    public UserDTO getById(int id) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDTO(userEntity);
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = repository.findAll();

        return mapper.toDTOs(userEntities);
    }

    @Transactional
    public UserDTO update(int id, UserDTO updatedUser) {
        UserEntity userEntity = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        mapper.updateEntity(userEntity, updatedUser);

        return mapper.toDTO(userEntity);
    }

    @Transactional
    public UserDTO register(RegisteredUserDTO user) {
        UserEntity userEntity = repository.save(mapper.toEntity(user));

        return mapper.toDTO(userEntity);
    }

}
