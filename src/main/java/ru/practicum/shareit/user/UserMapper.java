package ru.practicum.shareit.user;


import org.mapstruct.*;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

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
