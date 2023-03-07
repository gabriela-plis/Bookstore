package app.backend.user;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity user);

    UserEntity toEntity(UserDTO user);

    List<UserEntity> toEntities(List<UserDTO> userDTOs);

    List<UserDTO> toDTOs(List<UserEntity> userDTOs);
}
