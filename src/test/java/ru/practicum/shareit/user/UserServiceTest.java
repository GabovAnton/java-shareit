package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemShortAvailability;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private final EntityManager em;

    LocalDateTime currentDate = LocalDateTime
            .of(2022, 12, 10, 5, 5, 5, 5);

    @Mock
    UserRepository userRepository;
    @Mock
    private  UserMapper userMapper;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @InjectMocks
    private UserService userService = new UserServiceImpl(
            userRepository,
            userMapper
    );

    @Test
    void getUser_WrongIdShouldThrowException() {
        Long userId = 100l;
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUser(userId);
        });
        assertThat(entityNotFoundException.getMessage(),
                equalTo("user with id: " + userId + " doesn't exists"));
    }

    @Test
    void getAll_ShouldReturnListOfEntity() {
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "userMapper", userMapper);

        User user = makeUser();
        List<User> expectedUsers = List.of(user);

        when(userRepository.findAll())
                .thenReturn(expectedUsers);

        List<UserDto> allUserDTO = userService.getAll();
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
        List<UserDto> userDtos = List.of(userDto);

        assertThat(allUserDTO.size(), equalTo(userDtos.size()));
        assertThat(allUserDTO.get(0).getName(), equalTo(userDtos.get(0).getName()));


    }

    @Test
    void save_ShouldReturnSameEntity() {
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "userMapper", userMapper);
        User user = makeUser();

        when(userRepository.save(any()))
                .thenReturn(user);
        User savedUser = userService.save(user);
        assertThat(savedUser, equalTo(user));

    }

    @Test
    void update_WrongIdShouldThrowException() {
        Long userId = 100l;
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        UserUpdateDto userUpdateDto = makeUserUpdateDto();
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            userService.update(userUpdateDto, 100L, 100L);
        });
        assertThat(entityNotFoundException.getMessage(),
                equalTo("user id: " + userId + " not found"));
    }

    @Test
    void deleteShouldNotThrowException() {
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        doNothing().when(userRepository).deleteById(anyLong());
        assertThat(userService.delete(100L, 110L), equalTo(true));

    }

    @Test
    void existsById_ShouldReturnTrue() {
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
       when(userRepository.existsById(anyLong()))
               .thenReturn(true);
        assertThat(userService.existsById(100L), equalTo(true));

    }

    private User makeUser() {
        User user = new User();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");

        return user;
    }

    private UserUpdateDto makeUserUpdateDto() {
        UserUpdateDto user = new UserUpdateDto();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");

        return user;
    }


    private UserDto makeUserDto() {
        UserDto user = new UserDto();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");
        user.setRegistrationDate(currentDate.minusDays(15));
        return user;
    }



}