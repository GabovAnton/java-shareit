package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User getUser(long id);

    List<UserDto> getAll();

    User save(User user);

    UserDto update(UserUpdateDto userUpdateDto, Long userToUpdateId, Long userId);

    boolean delete(Long userToDeleteId, Long userId);

    Boolean existsById(long userId);
}