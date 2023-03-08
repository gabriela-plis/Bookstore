package app.backend.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDTO findByLoginData(LoginDTO loginData) {
        UserEntity userEntity = repository.findByEmailAndPasswordAndEmployee(loginData.email(), loginData.password(), loginData.isEmployee())
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDTO(userEntity);
    }

    public UserDTO findById(int id) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDTO(userEntity);
    }

    public UserDTO update(UserDTO user) {
        return save(user);
    }

    public UserDTO register(UserDTO user) {
        return save(user);
    }

    private UserDTO save(UserDTO user) {
        UserEntity userEntity = repository.save(mapper.toEntity(user));

        return mapper.toDTO(userEntity);
    }

}
