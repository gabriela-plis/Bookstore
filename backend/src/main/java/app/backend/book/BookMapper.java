package app.backend.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "type", source = "bookType.name")
    BookDTO toDTO(BookEntity book);

    BookEntity toEntity(BookDTO book);

    List<BookDTO> toDTOs(List<BookEntity> books);

}
