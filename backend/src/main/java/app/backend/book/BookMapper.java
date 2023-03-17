package app.backend.book;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toDTO(BookEntity book);

    BookEntity toEntity(BookDTO book);

    List<BookDTO> toDTOs(List<BookEntity> books);

}
