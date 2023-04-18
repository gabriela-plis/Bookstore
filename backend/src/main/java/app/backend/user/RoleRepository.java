package app.backend.user;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface RoleRepository extends ListCrudRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName (String name);
}
