package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with id: " + id + " doesn't exists"));
    }

    @Override
    public List<UserDto> getAll() {

        List<UserDto> userList = userRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(UserMapper.INSTANCE::userToUserDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        log.debug("all items requested: {}", userList.size());
        return userList;
    }

    @Override
    public User save(User user) {
            User savedUser = userRepository.save(user);
            log.debug("new user created: {}", savedUser);
            return savedUser;
    }

    @Override
    public UserDto update(UserDto userDto) {

        User userToUpdate = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("user id: " + userDto.getId() + " not found"));
        UserMapper.INSTANCE.updateUserFromUserDto(userDto, userToUpdate);
        userRepository.save(userToUpdate);

        return UserMapper.INSTANCE.userToUserDto(userToUpdate);
    }

    @Override
    public boolean delete(long userId) {

        userRepository.deleteById(userId);
        return true;
    }


}
