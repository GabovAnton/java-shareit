package ru.practicum.shareit.request;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemLastBookingDto;
import ru.practicum.shareit.item.ItemNextBookingDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    LocalDateTime currentDate = LocalDateTime
            .of(2022, 12, 10, 5, 5, 5, 5);
    @Mock
    private RequestMapper requestMapper;
    @Mock
    private UserService userService;
    @Captor
    private ArgumentCaptor<Request> requestArgumentCaptor;
    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private RequestService requestService =
            new RequestServiceImpl(
                    requestRepository,
                    userService,
                    requestMapper

            );

    @Test
    void getAll_ByWrongUserShouldThrowException() {

        ReflectionTestUtils.setField(requestService, "userService", userService);

        Long userId = 100L;
        when(userService.getUser(anyLong()))
                .thenThrow(new EntityNotFoundException("user with id: " + userId + " doesn't exists"));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            requestService.getAll(100L);
        });
        assertThat(entityNotFoundException.getMessage(),
                equalTo("user with id: " + userId + " doesn't exists"));

    }

    @Test
    void save_ShouldNotThrowException() {
        ReflectionTestUtils.setField(requestService, "requestRepository", requestRepository);
        ReflectionTestUtils.setField(requestService, "userService", userService);
        ReflectionTestUtils.setField(requestService, "requestMapper", requestMapper);
        RequestDto requestDto = makeRequestDto();

        when(requestMapper.requestDtoToRequest(requestDto))
                .thenReturn(makeRequest());
        when(userService.getUser(anyLong()))
                .thenReturn(makeUser());

        requestService.saveRequest(requestDto, 100L);

        Mockito.verify(requestRepository).save(requestArgumentCaptor.capture());
        Request savedRequest = requestArgumentCaptor.getValue();

        assertThat(savedRequest.getDescription(), equalTo(requestDto.getDescription()));
        assertThat(savedRequest.getId(), equalTo(requestDto.getId()));

    }

    private ItemDto makeItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(100L);
        itemDto.setName("thing");
        itemDto.setDescription("just simple thing");
        itemDto.setAvailable(true);
        itemDto.setRequestId(100L);
        itemDto.setLastBooking(new ItemLastBookingDto(100L, 100L));
        itemDto.setNextBooking(new ItemNextBookingDto(100L, 100L));

        CommentDto commentDto = new CommentDto(100L,
                "good thing",
                101L,
                "Artur",
                LocalDateTime.now().minusDays(100));

        itemDto.setComments(Set.of(commentDto));
        itemDto.setOwner(new UserDto());

        return itemDto;
    }

    private UserDto makeUserDto() {
        UserDto user = new UserDto();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");
        user.setRegistrationDate(currentDate.minusDays(15));
        return user;
    }

    private User makeUser() {
        User user = new User();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");

        return user;
    }

    private RequestDto makeRequestDto() {
        return new RequestDto(
                100L,
                "simple test description",
                makeUserDto(),
                currentDate.minusDays(1)

        );

    }

    private Request makeRequest() {
        Request request = new Request();
        request.setDescription("simple test description");
        request.setRequester(makeUser());
        request.setCreated(currentDate.minusDays(1));
        request.setId(100L);
        return request;
    }

}