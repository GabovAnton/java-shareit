package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

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


}
