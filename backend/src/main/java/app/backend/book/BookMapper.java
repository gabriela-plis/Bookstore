package app.backend.book;

import app.backend.book.bookType.BookTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR, uses = BookTypeMapper.class, injectionStrategy = CONSTRUCTOR)
public interface BookMapper {

    BookDTO toDTO(BookEntity book);

    @Mapping(target = "ownerUsers", ignore = true)
    BookEntity toEntity(BookDTO book);

    List<BookDTO> toDTOs(List<BookEntity> books);

    @Mapping(target = "ownerUsers", ignore = true)
    void updateEntity(@MappingTarget BookEntity bookToUpdate, BookDTO updatedBook);
}
