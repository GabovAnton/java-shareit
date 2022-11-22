package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.dao.Dao;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {
    private final UserDao userDao;
    @Override
    public User getUser(long id) {
        return userDao.get(id).orElseThrow(()->new EntityNotFoundException("user with id: " + id + " doesn't exists"));
    }

    @Override
    public List<UserDto> getAll() {

        List<UserDto> userList = userDao.getAll().stream()
                .filter(Objects::nonNull)
                .map(UserMapper::toUserDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        log.debug("all items requested: {}", userList.size());
        return null;
    }

    @Override
    public Long save(User user) {
        return null;
    }

    @Override
    public List<User> search(String text, long userId) {
        return null;
    }

    @Override
    public UserDto update(UserDto userDto) {
        return null;
    }
}
