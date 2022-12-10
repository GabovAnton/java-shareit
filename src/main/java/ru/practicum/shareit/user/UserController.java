package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @JsonView(UserDto.SimpleView.class)
    @GetMapping("{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userMapper.userToUserDto(userService.getUser(userId));
    }

    @JsonView(UserDto.SimpleView.class)
    @GetMapping()
    public List<UserDto> getAll() {
        return userService.getAll();
    }


    @JsonView(UserDto.SimpleView.class)
    @PostMapping()
    public UserDto create(@Validated(UserDto.New.class) @RequestBody UserDto userDto) {
        User savedUser = userService.save(userMapper.userDtoToUser(userDto));
        return userMapper.userToUserDto(savedUser);
    }


    @JsonView(UserDto.SimpleView.class)
    @PatchMapping("{userId}")
    public UserDto update(@PathVariable long userId, @Validated(UserDto.Update.class) @RequestBody UserDto userDto) {
        userDto.setId(userId);
        return userService.update(userDto);
    }

    @JsonView(UserDto.SimpleView.class)
    @DeleteMapping("{userId}")
    public boolean delete(@PathVariable long userId) {
        return userService.delete(userId);
    }


}
