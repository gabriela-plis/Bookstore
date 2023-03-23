package app.backend.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
public interface BookTypeMapper {

    @Mapping(target = "books", ignore = true)
    BookTypeEntity toEntity(BookTypeDTO bookType);

    BookTypeDTO toDTO(BookTypeEntity bookType);

    List<BookTypeDTO> toDTOs(List<BookTypeEntity> bookTypes);
}
