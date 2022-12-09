package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.ShareItValidationException;

import javax.servlet.http.HttpServletRequest;
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
        return UserMapper.INSTANCE.userToUserDto(userService.getUser(userId));
    }

    @JsonView(UserDto.SimpleView.class)
    @GetMapping()
    public List<UserDto> getAll() {
        return userService.getAll();
    }


    @JsonView(UserDto.SimpleView.class)
    @PostMapping()
    public UserDto create(@Validated(UserDto.New.class) @RequestBody UserDto userDto) {
        User savedUser = userService.save(UserMapper.INSTANCE.userDtoToUser(userDto));
        return UserMapper.INSTANCE.userToUserDto(savedUser);
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
