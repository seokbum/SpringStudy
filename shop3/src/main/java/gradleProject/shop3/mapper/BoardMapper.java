package gradleProject.shop3.mapper;

import gradleProject.shop3.domain.Board;
import gradleProject.shop3.dto.BoardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardMapper {


    @Mapping(target = "fileurl", ignore = true)
    Board toEntity(BoardDto boardDto);

    @Mapping(source = "fileurl", target = "fileurl")
    @Mapping(target = "file1", ignore = true)
    BoardDto toDto(Board board);
}