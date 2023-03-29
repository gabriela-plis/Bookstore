package app.backend.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDTO findById(int id) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDTO(userEntity);
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
