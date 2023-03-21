package app.backend.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity user);

    UserEntity toEntity(UserDTO user);

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(RegisteredUserDTO user);

    void updateEntity(@MappingTarget UserEntity userToUpdate, UserDTO updatedUser);
}
