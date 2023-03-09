package app.backend.user;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity user);

    UserEntity toEntity(UserDTO user);
    void updateEntity(@MappingTarget UserEntity userEntity, UserDTO updatedUser);
}
