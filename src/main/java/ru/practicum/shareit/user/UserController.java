package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long userId) {
        return ResponseEntity.ok(UserMapper.INSTANCE.userToUserDto(userService.getUser(userId)));
    }

    @GetMapping()
    public List<UserDto> getAll() {
        return userService.getAll();
    }


    @PostMapping()
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User savedUser = userService.save(UserMapper.INSTANCE.userDtoToUser(userDto));
        return UserMapper.INSTANCE.userToUserDto(savedUser);
    }

    @PatchMapping("{userToUpdateId}")
    public UserDto update(@PathVariable long userToUpdateId, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.update(userUpdateDto, userToUpdateId);
    }

    @DeleteMapping("{userToDeleteId}")
    public boolean delete(@PathVariable long userToDeleteId) {
        return userService.delete(userToDeleteId);
    }


}
