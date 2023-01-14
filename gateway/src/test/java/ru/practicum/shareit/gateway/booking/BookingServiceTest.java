package ru.practicum.shareit.gateway.booking;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.gateway.booking.dto.BookingCreateDto;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8089)
@SpringBootTest(properties = "SHAREIT_SERVER_URL=http://localhost:8089")

@ExtendWith(SpringExtension.class)
class BookingServiceTest {

    LocalDateTime currentDate = LocalDateTime.of(2022, 12, 10, 5, 5, 5, 5);

    /*@Autowired
    CacheManager cacheManager;*/
    @Autowired
    BookingFeignClient bookingFeignClient;

    @Test
    void createShouldReturnNewDto() {

        stubFor(post("/bookings")
                .withRequestBody(equalToJson("{\"id\" : null, \"start\" : \"2022-12-10T05:05:05.000000005\", \"end\" : " +
                                             "\"2022-12-29T05:05:05.000000005\", \"itemId\" : 100, \"userId\" : 100}",
                        true,
                        true))
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("oneBooking-response.json")));
        BookingDto bookingDto = bookingFeignClient.create(1L, makeBookingCreateDto());

        assertThat(bookingDto).isNotNull().extracting(BookingDto::getItem).extracting(ItemDto::getId).isEqualTo(100L);

    }

    @Test
    void updateShouldReturnUpdatedDto() {

        stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .patch(urlEqualTo("/bookings/1?approved=false"))
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("updateBooking-response.json")));
        BookingDto bookingDto = bookingFeignClient.update(1L, false, 1L);

        assertThat(bookingDto).isNotNull().extracting(BookingDto::getStatus).isEqualTo("REJECTED");
    }

    @Test
    void getBookingShouldReturnDto() {

        stubFor(get("/bookings/1").willReturn(WireMock
                .aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBodyFile("oneBooking-response.json")));

        BookingDto bookingDto = bookingFeignClient.getBookingById(1L, 1L);
        assertThat(bookingDto).isNotNull().extracting(BookingDto::getItem).extracting(ItemDto::getId).isEqualTo(100L);

    }

    @Test
    void getBookingsByStateShouldReturnListDto() {

        stubFor(get("/bookings?from=0&size=10&state=ALL").willReturn(WireMock
                .aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBodyFile("bookings-response.json")));

        List<BookingDto> bookingsDto = bookingFeignClient.getBookingByState(1L, "0", "10", "ALL");
        assertThat(bookingsDto)
                .isNotNull()
                .hasSize(1)
                .element(0)
                .extracting(BookingDto::getItem)
                .extracting(ItemDto::getId)
                .isEqualTo(100L);

    }

    @Test
    void getBookingsByOwnerShouldReturnListDto() {

        stubFor(get("/bookings/owner?from=0&size=10&state=ALL").willReturn(WireMock
                .aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBodyFile("bookings-response.json")));

        List<BookingDto> bookingsDto = bookingFeignClient.getItemsByStateAndOwner("0", "10", "ALL", 1L);
        assertThat(bookingsDto)
                .isNotNull()
                .hasSize(1)
                .element(0)
                .extracting(BookingDto::getItem)
                .extracting(ItemDto::getId)
                .isEqualTo(100L);

    }

    private BookingCreateDto makeBookingCreateDto() {

        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setUserId(100L);
        bookingCreateDto.setStart(currentDate);
        bookingCreateDto.setEnd(currentDate.plusDays(19));
        bookingCreateDto.setItemId(100L);
        return bookingCreateDto;
    }

}

