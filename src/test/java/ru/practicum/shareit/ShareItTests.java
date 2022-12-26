package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RequiredArgsConstructor(onConstructor_ = @Autowired)

@Sql(scripts = {"classpath:/schema.sql", "classpath:/SampleData.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShareItTests {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    private final RequestMapper requestMapper;
    private final RequestService requestService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testDatabaseIsNotEmpty() {
        List<User> allUsers = userRepository.findAll();
        assertThat(allUsers, hasSize(5));

        List<Item> allItems = itemRepository.findAll();
        assertThat(allItems, hasSize(9));

    }

    @Test
    public void CreateUserShouldNotThrowExceptionOnSave() {
        UserDto userOne = new UserDto(null, "testName", "test@gmail.com", null);

        assertThat(userRepository.save(userMapper.userDtoToUser(userOne)).getName(),
                equalToIgnoringCase("testName"));

    }

    @Test
    public void CreateUserWithDuplicateEmailShouldThrowExceptionOnSave() {

        UserDto userOne = new UserDto(null, "testName", "agabov@gmail.com", null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(userMapper.userDtoToUser(userOne));
        });

    }

    @Test
    public void UpdateUserWithDuplicateEmailShouldThrowExceptionOnSave() {
        User user = userRepository.findById(1L).get();
        user.setEmail("ivan@gmail.com");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
        });
    }

    @Test
    public void UpdateUserShouldNotThrowExceptionOnSave() {
        User user = userRepository.findById(1L).get();
        user.setEmail("anton2@gmail.com");

        assertThat(userRepository.save(user).getEmail(), equalToIgnoringCase("anton2@gmail.com"));

    }

    @Test
    public void DeleteUserShouldNotThrowExceptionOnSave() {
        User user = userRepository.findById(1L).get();

        userRepository.delete(user);

    }

    @Test
    public void CreateItemShouldNotThrowExceptionOnSave() {
        Item item = new Item();
        item.setName("Test item");
        item.setAvailable(true);
        item.setDescription("new interesting item");
        item.setOwner(userRepository.findById(1L).get());

        assertThat(itemRepository.save(item).getName(), equalToIgnoringCase("Test item"));

    }


    @Test
    public void SearchInDescriptionShouldReturnOneRecord() {
        List<Item> searchResult = itemRepository.findByNameOrDescription("%отверт%");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.stream().anyMatch(x -> x.getName().equalsIgnoreCase("Отвертка")), is(true));


    }

    @Test
    public void GetAllItemsForUserOneShouldReturnTwoRecords() {
        List<Item> searchResult = itemRepository.findByOwnerId(1L);

        assertThat(searchResult, hasSize(2));
        assertThat(searchResult.stream().anyMatch(x -> x.getName().equals("Аккумуляторная дрель")), is(true));
    }

    @Test
    public void CreateBookingInFutureOnItem2ByUserShouldNotThrowError() {
        Booking booking = new Booking();
        booking.setItem(itemRepository.findByOwnerId(1L).stream().findFirst().get());
        booking.setBooker(userRepository.findById(3L).get());
        booking.setEnd(LocalDateTime.now().plusDays(5));
        booking.setStart(LocalDateTime.now().plusMinutes(3));


        assertThat(bookingRepository.save(booking)
                        .getItem().getName(),
                equalTo("Аккумуляторная дрель"));

        List<Booking> future = bookingRepository.SearchBookingsByBookerInFutureTime(3L, LocalDateTime.now());
        assertThat(future, hasSize(1));
        assertThat(future.stream().anyMatch(x -> x.getId().equals(1L)), is(true));

    }

    @Test
    public void CreateBookingInPastOnItem2ByUserShouldNotThrowError() {
        Booking booking = new Booking();
        booking.setItem(itemRepository.findByOwnerId(1L).stream().findFirst().get());
        booking.setBooker(userRepository.findById(3L).get());
        booking.setEnd(LocalDateTime.now().minusDays(30));
        booking.setStart(LocalDateTime.now().minusDays(35));

        assertThat(bookingRepository.save(booking)
                        .getItem().getName(),
                equalTo("Аккумуляторная дрель"));

        List<Booking> past = bookingRepository.SearchBookingsByBookerInPastTime(3L, LocalDateTime.now());

        assertThat(past, hasSize(1));
        assertThat(past.stream().anyMatch(x -> x.getId().equals(1L)), is(true));


    }

    @Test
    public void CreateBookingInCurrentOnItem2ByUserShouldNotThrowError() {
        Booking booking = new Booking();
        booking.setItem(itemRepository.findByOwnerId(1L).stream().findFirst().get());
        booking.setBooker(userRepository.findById(3L).get());
        booking.setStart(LocalDateTime.now().minusSeconds(1));
        booking.setEnd(LocalDateTime.now().plusSeconds(10));

        assertThat(bookingRepository.save(booking)
                        .getItem().getName(),
                equalTo("Аккумуляторная дрель"));

        List<Booking> current = bookingRepository.SearchBookingsByBookerInPresentTime(3L, LocalDateTime.now());
        assertThat(current.stream().anyMatch(x -> x.getId().equals(1L)), is(true));

    }

    @Test
    public void CreateCommentOnItem2ByUserShouldNotThrowError() {
        Comment comment = new Comment();
        comment.setItem(itemRepository.findByOwnerId(1L).stream().findFirst().get());
        comment.setText("test comment");
        comment.setAuthor(userRepository.findById(3L).get());
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        Set<Comment> itemComments = itemRepository
                .findByOwnerId(1L).stream().filter(x -> x.getId().equals(1L)).findFirst().get().getItemComments();

        assertThat(itemComments, hasSize(1));
        assertThat(itemComments.stream().anyMatch(x -> x.getText().equals("test comment")), is(true));

    }

    @Test
    public void Fetch10ElementsFrom26OfTotal30ElementShouldReturnExactFrom5To1() {
        List<User> users = new ArrayList<>(userRepository.findAll());
        int day = 1;
        for (int i = 0; i < 30; i++) {
            Request request = new Request();
            request.setCreated(LocalDateTime.now().plusDays(day++));

            int length = 50;
            boolean useLetters = true;
            boolean useNumbers = false;
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
            request.setDescription(generatedString);

            Collections.shuffle(users);
            request.setRequester(users.get(0));

            RequestDto requestDto = requestMapper.requestToRequestDto(request);
            requestService.saveRequest(requestDto, users.get(0).getId());

        }

        List<Long> collect = requestService.getAllFromOthers(26, 10, 6L).stream()
                .map(RequestWithProposalsDto::getId).collect(Collectors.toList());

        assertThat(collect, hasSize(5));
        assertThat(collect, Matchers.containsInRelativeOrder(List.of(5L, 4L, 3L, 2L, 1L).toArray()));

    }


}
