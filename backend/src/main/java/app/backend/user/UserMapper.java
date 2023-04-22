package app.backend.user;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    UserDTO toDTO(UserEntity user);

    List<UserDTO> toDTOs(List<UserEntity> userEntities);

    @Mapping(target = "roles", expression = "java(new ArrayList<RoleEntity>())")
    @Mapping(target = "books", expression = "java(new ArrayList<RoleEntity>())")
    UserEntity toEntity(UserDTO user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", expression = "java(new ArrayList<RoleEntity>())")
    @Mapping(target = "books", expression = "java(new ArrayList<RoleEntity>())")
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
