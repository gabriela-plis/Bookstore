package app.backend.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    UserDTO toDTO(UserEntity user);

    List<UserDTO> toDTOs(List<UserEntity> userEntities);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "books", ignore = true)
    UserEntity toEntity(UserDTO user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "books", ignore = true)
    UserEntity toEntity(RegisteredUserDTO user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntity(@MappingTarget UserEntity userToUpdate, UserDTO updatedUser);

    default List<String> mapUserRoles(List<RoleEntity> userRoles) {
        return userRoles.stream()
            .map(RoleEntity::getName)
            .toList();
    }
}
