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
    @GetMapping("{itemId}")
    public UserDto getUserById(@PathVariable long userId) {
        return UserMapper.toUserDto(userService.getUser(userId));
    }

    @JsonView(UserDto.SimpleView.class)
    @GetMapping()
    public List<UserDto> getAll() {
        return userService.getAll();
    }


    @JsonView(UserDto.SimpleView.class)
    @PostMapping()
    public UserDto create(@Validated(UserDto.New.class) @RequestBody UserDto userDto) {
        long id = userService.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(userService.getUser(id));
    }


    @JsonView(UserDto.SimpleView.class)
    @PatchMapping("{userid}")
    public UserDto update(@PathVariable long userid, @Validated(UserDto.Update.class) @RequestBody UserDto userDto) {
        userDto.setId(userid);
        return userService.update(userDto);
    }
}
