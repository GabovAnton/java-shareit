package ru.practicum.shareit.user;


import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.BookingMapper;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserFromUserUpdateDto(UserUpdateDto userUpdateDto, @MappingTarget User user);

    @AfterMapping
    default void linkItems(@MappingTarget User user) {
        user.getItems().forEach(item -> item.setOwner(user));
    }

    @AfterMapping
    default void linkComments(@MappingTarget User user) {
        user.getComments().forEach(Comment -> Comment.setAuthor(user));
    }
}
