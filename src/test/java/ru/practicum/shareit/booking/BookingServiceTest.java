package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RequiredArgsConstructor(onConstructor_ = @Autowired)

@Sql(scripts = {"classpath:/schema.sql", "classpath:/SampleData.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceTest {

    @Test
    void save() {

        /*OrderService orderService = new OrderService();

        CustomerService customerService = Mockito.mock(CustomerService.class);
        BookService bookService = Mockito.mock(BookService.class);
        OrderDao orderDao = Mockito.mock(OrderDao.class);

        ReflectionTestUtils.setField(orderService, "customerService", customerService);
        ReflectionTestUtils.setField(orderService, "bookService", bookService);
        ReflectionTestUtils.setField(orderService, "orderDao", orderDao);*/
    }

    @Test
    void changeBookingStatus() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getBookingByState() {
    }

    @Test
    void getBookingByStateAndOwner() {
    }

    private BookingDto makeBookingDto(LocalDateTime start, LocalDateTime end, BookingStatus status, Long bookerId, long itemId) {
        BookingDto dto = new BookingDto();
        dto.setStart(start);
        dto.setEnd(end);
        dto.setStatus(status);
        dto.setItem(new ItemDto());//todo
        dto.setBooker(new UserDto());//todo

        return dto;
    }
}