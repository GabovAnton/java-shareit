package ru.practicum.shareit.gateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.booking.dto.BookingCreateDto;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;
import ru.practicum.shareit.gateway.booking.dto.BookingState;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        BookingState state = BookingState.from(stateParam).orElseThrow(() -> new IllegalArgumentException(
                "Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);

        return bookingService.getBookingByState(userId, from, size, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        BookingState state = BookingState.from(stateParam).orElseThrow(() -> new IllegalArgumentException(
                "Unknown state: " + stateParam));
        log.info("Get booking by Owner with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingService.getItemsByStateAndOwner(userId, from, size, state);
    }

    @PostMapping
    public BookingDto bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody BookingCreateDto createDto) {

        log.info("Creating booking {}, userId={}", createDto, userId);
        return bookingService.create(userId, createDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long bookingId) {

        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingService.getBooking(userId, bookingId);
    }

    @PatchMapping("{bookingId}")
    public BookingDto updateBooking(@PathVariable long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Update booking {}, userId={}", bookingId, userId);
        return bookingService.update(userId, bookingId, approved);
    }

}
