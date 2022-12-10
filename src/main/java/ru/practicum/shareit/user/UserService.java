package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User getUser(long id);

    List<UserDto> getAll();

    User save(User user);

    UserDto update(UserDto userDto);

    boolean delete(long userId);
   Boolean existsById(long userId);
}