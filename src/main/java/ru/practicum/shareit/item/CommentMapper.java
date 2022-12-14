package ru.practicum.shareit.item;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "authorName", target = "author.name")
    Comment commentDtoToComment(CommentDto commentDto);

    @InheritInverseConfiguration(name = "commentDtoToComment")
    CommentDto commentToCommentDto(Comment comment);

    Set<CommentDto> map(Set<Comment> comments);

}
