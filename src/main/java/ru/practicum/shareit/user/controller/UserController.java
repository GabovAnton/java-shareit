package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @JsonView(UserDto.SimpleView.class)
    @GetMapping("{userId}")
    public UserDto getUserById(@PathVariable long userId) {
       // return UserMapper.toUserDto(userService.getUser(userId));
        return null;
    }

    @JsonView(UserDto.SimpleView.class)
    @GetMapping()
    public List<UserDto> getAll() {
        return userService.getAll();
    }


    @JsonView(UserDto.SimpleView.class)
    @PostMapping()
    public UserDto create(@Validated(UserDto.New.class) @RequestBody UserDto userDto) {
       /* long id = userService.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(userService.getUser(id));*/
        return  null;
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
