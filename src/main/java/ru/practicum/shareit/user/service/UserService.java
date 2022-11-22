package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User getUser(long id);
    List<UserDto> getAll();

    Long save(User user);

    List<User> search(String text, long userId);
    UserDto update(UserDto userDto);
}
