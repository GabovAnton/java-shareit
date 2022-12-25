package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ItemController.class)
/*@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc*/

class ItemControllerTest {

   /* @Autowired
    ObjectMapper mapper;*/

    /*@Mock
    ItemMapper itemMapper;

    @Mock
    CommentMapper commentMapper;*/

   /* @MockBean
    ItemService itemMockService;

    @InjectMocks
    private ItemController itemController;*/

    LocalDateTime currentDate = LocalDateTime
            .of(2022, 12, 10, 5, 5, 5, 5);
  /*  @Autowired
    private MockMvc mvc;*/

    @Test
    void getItemById() {
     /*   ReflectionTestUtils.setField(itemController, "itemService", itemMockService);
        ReflectionTestUtils.setField(itemController, "itemMapper", itemMapper);
        ReflectionTestUtils.setField(itemController, "commentMapper", commentMapper);*/

       /* ItemDto itemDto = makeItemDto();

        when(itemMockService.getItemDto(anyLong(), anyLong()))
                .thenReturn(itemDto);

        when(itemMapper.itemToItemDto(any()))
                .thenReturn(itemDto);

        ResponseEntity<ItemDto> response = itemController.getItemById(1L, 1L);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(itemDto, equalTo(response.getBody()));*/
    }

    @Test
    void getAll() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void searchByQuery() {
    }

    @Test
    void postComment() {
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

    private Item makeItem() {
        Item item = new Item();
        item.setId(100L);
        item.setName("thing");
        item.setDescription("just simple thing");
        item.setAvailable(true);
        item.setRequestId(100L);
        item.setOwner(new User());
        Booking bookingOne = makeBooking();
        Booking bookingTwo = makeBooking();
        bookingTwo.setStart(currentDate.plusDays(5));
        bookingTwo.setEnd(currentDate.plusDays(15));

        Set<Booking> itemBookings = Set.of(bookingOne, bookingTwo);

        item.setItemBookings(itemBookings);


        item.setItemComments(Set.of(makeComment()));


        return item;
    }

    private Booking makeBooking() {
        Booking booking = new Booking();
        booking.setBooker(new User());
        booking.setStart(currentDate.minusDays(2));
        booking.setEnd(currentDate.plusDays(2));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(new Item());
        booking.getBooker().setId(100L);
        booking.getItem().setId(100L);
        booking.getItem().setOwner(makeUser());
        booking.getItem().getOwner().setId(100L);

        return booking;
    }

    private Comment makeComment() {
        Comment comment = new Comment();
        comment.setId(100L);
        comment.setText("good thing");
        comment.setAuthor(makeUser());
        return comment;
    }

    private User makeUser() {
        User user = new User();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");
        return user;
    }
}