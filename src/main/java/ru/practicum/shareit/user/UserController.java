package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserMapper userMapper;


    @GetMapping("{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userMapper.userToUserDto(userService.getUser(userId));
    }


    @GetMapping()
    public List<UserDto> getAll() {
        return userService.getAll();
    }


    @PostMapping()
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User savedUser = userService.save(userMapper.userDtoToUser(userDto));
        return userMapper.userToUserDto(savedUser);
    }

    @PatchMapping("{userId}")
    public UserDto update(@PathVariable long userId, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.update(userUpdateDto, userId);
    }

    @DeleteMapping("{userId}")
    public boolean delete(@PathVariable long userId) {
        return userService.delete(userId);
    }


}
