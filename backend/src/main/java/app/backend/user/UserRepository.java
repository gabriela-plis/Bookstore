package app.backend.user;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmailAndPasswordAndEmployee(String email, String password, boolean employee);
}
