package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void contextLoads() {
    }

    @Test
    public void testDatabaseIsNotEmpty() {
        List<User> allUsers = userRepository.findAll();
        assertThat(allUsers)
                .isNotEmpty();

        List<Item> allItems = itemRepository.findAll();
        assertThat(allItems)
                .isNotEmpty();

    }

    @Test
    public void CreateUserShouldNotThrowExceptionOnSave() {
        UserDto userOne = new UserDto(null, "testName", "test@gmail.com", null);

        assertThat(userRepository.save(userMapper.userDtoToUser(userOne)))
                .extracting(User::getName)
                .isEqualTo("testName");
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

        assertThat(userRepository.save(user))
                .extracting(User::getEmail)
                .isEqualTo("anton2@gmail.com");
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

        assertThat(itemRepository.save(item)
                .getName()
                .equals("Test item"));
    }


    @Test
    public void SearchInDescriptionShouldReturnOneRecord() {
        List<Item> searchResult = itemRepository.findByNameOrDescription("%отверт%");

        assertThat(searchResult)
                .isNotEmpty()
                .hasSize(1)
                .extracting(Item::getDescription)
                .contains("Аккумуляторная отвертка");
    }

    @Test
    public void GetAllItemsForUserOneShouldReturnTwoRecords() {
        List<Item> searchResult = itemRepository.findByOwner_Id(1L);

        assertThat(searchResult)
                .isNotEmpty()
                .hasSize(2)
                .extracting(Item::getName)
                .contains("Отвертка")
                .contains("Аккумуляторная дрель");
    }

    @Test
    public void CreateBookingInFutureOnItem2ByUserShouldNotThrowError() {
        Booking booking = new Booking();
        booking.setItem(itemRepository.findByOwner_Id(1L).stream().findFirst().get());
        booking.setBooker(userRepository.findById(3L).get());
        booking.setEnd(LocalDateTime.now().plusDays(5));
        booking.setStart(LocalDateTime.now().plusMinutes(3));

        assertThat(bookingRepository.save(booking)
                .getItem().getName()
                .equals("Аккумуляторная дрель"));

        List<Booking> future = bookingRepository.findByBooker_IdAndStartIsAfter(3L, LocalDateTime.now());
        assertThat(future).isNotEmpty()
                .hasSize(1)
                .extracting(Booking::getId)
                .contains(1L);

    }

    @Test
    public void CreateBookingInPastOnItem2ByUserShouldNotThrowError() {
        Booking booking = new Booking();
        booking.setItem(itemRepository.findByOwner_Id(1L).stream().findFirst().get());
        booking.setBooker(userRepository.findById(3L).get());
        booking.setEnd(LocalDateTime.now().minusDays(30));
        booking.setStart(LocalDateTime.now().minusDays(35));

        assertThat(bookingRepository.save(booking)
                .getItem().getName()
                .equals("Аккумуляторная дрель"));

        List<Booking> future = bookingRepository.findByBooker_IdAndEndIsBefore(3L, LocalDateTime.now());
        assertThat(future).isNotEmpty()
                .hasSize(1)
                .extracting(Booking::getId)
                .contains(1L);
    }

    @Test
    public void CreateBookingInCurrentOnItem2ByUserShouldNotThrowError() {
        Booking booking = new Booking();
        booking.setItem(itemRepository.findByOwner_Id(1L).stream().findFirst().get());
        booking.setBooker(userRepository.findById(3L).get());
        booking.setStart(LocalDateTime.now().minusSeconds(1));
        booking.setEnd(LocalDateTime.now().plusSeconds(10));

        assertThat(bookingRepository.save(booking)
                .getItem().getName()
                .equals("Аккумуляторная дрель"));

        List<Booking> future = bookingRepository.findByBooker_IdCurrent(3L, LocalDateTime.now());
        assertThat(future).isNotEmpty()
                .hasSize(1)
                .extracting(Booking::getId)
                .contains(1L);
    }

    @Test
    public void CreateCommentOnItem2ByUserShouldNotThrowError() {
        Comment comment = new Comment();
        comment.setItem(itemRepository.findByOwner_Id(1L).stream().findFirst().get());
        comment.setText("test comment");
        comment.setAuthor(userRepository.findById(3L).get());
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        Set<Comment> itemComments = itemRepository
                .findByOwner_Id(1L).stream().filter(x -> x.getId().equals(1L)).findFirst().get().getItemComments();

        assertThat(itemComments).isNotEmpty()
                .hasSize(1)
                .extracting(Comment::getText)
                .contains("test comment");

    }

}
