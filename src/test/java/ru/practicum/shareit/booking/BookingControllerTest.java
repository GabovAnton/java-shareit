package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.ShareItValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
//@ContextConfiguration(classes = {BookingController.class, EntityNotFoundException.class, ErrorHandler.class})
@AutoConfigureMockMvc
class BookingControllerTest {

    @Mock
    private BookingService bookingMockService;

    @InjectMocks
    private BookingController bookingController;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private BookingMapper bookingMapper;
    private BookingDto bookingDto;
    private BookingCreateDto bookingCreateDto;
    private Booking booking;
    private MockMvc mvc;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {

        LocalDateTime currentDate = LocalDateTime.of(2022, 12, 10, 5, 5, 5, 5
        );
        //for testing json LocalDateTime
        mapper.registerModule(new JavaTimeModule());

        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        Item item = new Item();
        item.setName("test Item");
        item.setDescription("test description");
        item.setId(100L);
        booking = new Booking();
        booking.setBooker(new User());
        booking.setStart(currentDate.minusDays(2));
        booking.setEnd(currentDate.plusDays(2));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);

        booking.setId(100L);

        bookingDto = BookingMapper.INSTANCE.bookingToBookingDto(booking);

        bookingCreateDto = new BookingCreateDto(100L,
                currentDate.minusDays(2),
                currentDate.plusDays(2),
                1L,
                1L);

    }


    @Test
    void getBookingById_whenInvoked() {
        List<BookingDto> expectedBookings = List.of(BookingMapper.INSTANCE.bookingToBookingDto(new Booking()));
        when(bookingMockService.getBooking(anyLong(), anyLong()))
                .thenReturn(booking);
        when(bookingMapper.bookingToBookingDto(booking))
                .thenReturn(bookingDto);
        ResponseEntity<BookingDto> response = bookingController.getBookingById(1L, 1L);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(bookingDto, equalTo(response.getBody()));

    }

    @Test
    void create_ShouldReturnBookingDto() throws Exception {

        when(bookingMapper.bookingCreateDtoToBooking(any(), anyLong()))
                .thenReturn(booking);

        when(bookingMapper.bookingToBookingDto(booking))
                .thenReturn(bookingDto);

        when(bookingMockService.save(booking, 1L))
                .thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header("X-Sharer-User-Id", "1")
                )
                //  .andExpect(header().longValue("X-Sharer-User-Id", anyLong()));
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingCreateDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", equalTo(bookingDto.getStart().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.end", equalTo(bookingDto.getEnd().format(dateTimeFormatter))));
    }

    @Test
    void create_WithoutCustomHeaderShouldReturnException() throws Exception {

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400));
    }

    @Test
    void create_WrongUserShouldReturnEntityNotFoundException() throws Exception {

        when(bookingMockService.save(any(), anyLong()))
                .thenThrow(new EntityNotFoundException("foo"));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header("X-Sharer-User-Id", "1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        equalTo(EntityNotFoundException.class)))
                .andExpect(result -> assertThat(Objects.requireNonNull(result.getResolvedException()).getMessage(),
                        equalTo("foo")));
    }


    @Test
    void update_ShouldSetStatusToApproved() throws Exception {

        Booking updatedBooking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingMapper.bookingToBookingDto(updatedBooking))
                .thenReturn(BookingMapper.INSTANCE.bookingToBookingDto(booking));

        //verify(bookingMockService.changeBookingStatus())
        when(bookingMockService.changeBookingStatus(1L, true, 1L))
                .thenReturn(updatedBooking);

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", equalTo(BookingStatus.APPROVED.toString())));

    }

    @Test
    void update_ShouldThrowError() throws Exception {

        when(bookingMockService.changeBookingStatus(1L, true, 1L))
                .thenThrow(new IllegalArgumentException("foo"));

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "1")
                )
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertThat(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        equalTo(IllegalArgumentException.class)));
    }


    @Test
    void getBookingByState_ShouldReturnList() throws Exception {

        List<BookingDto> expectedBookings = List.of(bookingDto);
        when(bookingMockService.getBookingByState(anyInt(), anyInt(), anyLong(),any()))
                .thenReturn(expectedBookings);

       mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .param("from", "5")
                        .param("size", "5")
                        .param("state", "WAITING")
                        .header("X-Sharer-User-Id", "1")
                )
                .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].start", equalTo(bookingDto.getStart().format(dateTimeFormatter))))
               .andExpect(jsonPath("$[0].id", is(bookingCreateDto.getId()), Long.class))
               .andExpect(jsonPath("$[0].end", equalTo(bookingDto.getEnd().format(dateTimeFormatter))));
    }

    @Test
    void getItemsByStateAndOwner_ShouldReturnList() throws Exception {

        List<BookingDto> expectedBookings = List.of(bookingDto);
        when(bookingMockService.getBookingByStateAndOwner(anyInt(), anyInt(), anyLong(),any()))
                .thenReturn(expectedBookings);

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .param("from", "5")
                        .param("size", "5")
                        .param("state", "WAITING")
                        .header("X-Sharer-User-Id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].start", equalTo(bookingDto.getStart().format(dateTimeFormatter))))
                .andExpect(jsonPath("$[0].id", is(bookingCreateDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].end", equalTo(bookingDto.getEnd().format(dateTimeFormatter))));
    }
}