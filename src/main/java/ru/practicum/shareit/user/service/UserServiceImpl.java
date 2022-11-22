package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.ClassProperties;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public User getUser(long id) {
        return userDao.get(id).orElseThrow(() -> new EntityNotFoundException("user with id: " + id + " doesn't exists"));
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
        long id = userDao.save(user);
        log.debug("new user created: {}", user);
        return id;
    }


    @Override
    public UserDto update(UserDto userDto)  {
        User userToUpdate = userDao.get(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("user id: " + userDto.getId() + " not found"));

        Map<String, Object> classProperties = ClassProperties.getClassProperties(userDto, true);

        classProperties.forEach((k,v) -> {
            Class<User> clz = User.class;
            Arrays.stream(clz.getDeclaredMethods()).
                    filter(x -> x.getName().equals("set" + StringUtils.capitalize(k))).
                    findAny().ifPresent(y->ReflectionUtils.invokeMethod((y), userToUpdate, classProperties.get(k)));

        });
        return UserMapper.toUserDto(userToUpdate);    }


}
