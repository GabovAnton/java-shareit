package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    @PostMapping()
    public BookingDto create(@Valid @RequestBody BookingCreateDto bookingCreateDto, @RequestHeader("X-Sharer-User-Id") long userId) {

        Booking booking = bookingMapper.bookingCreateDtoToBooking(bookingCreateDto, userId);
        Booking savedBooking = bookingService.save(booking, userId);

        BookingDto bookingDto = bookingMapper.bookingToBookingDto(savedBooking);
        return bookingDto;
    }


    @PatchMapping("{bookingId}")
    public BookingDto update(@PathVariable long bookingId, @RequestParam Boolean approved, @RequestHeader("X-Sharer-User-Id") long userId) {
        Booking booking = bookingService.changeBookingStatus(bookingId, approved, userId);
        BookingDto bookingDto = bookingMapper.bookingToBookingDto(booking);

        return bookingDto;
    }


    @GetMapping("{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        Booking booking = bookingService.getBooking(userId, bookingId);

        return ResponseEntity.ok(bookingMapper.bookingToBookingDto(booking));

    }

    @GetMapping("")
    public List<BookingDto> getBookingByState(
            @RequestParam(required = false) @Min(value = 0, message = "from should not be less than 0") Integer from,
            @RequestParam(required = false) @Min(value = 0, message = "size should not be less than 1") Integer size,
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingByState(from, size, userId, state);

    }

    @GetMapping("/owner")
    public List<BookingDto> getItemsByStateAndOwner(
            @RequestParam(required = false) @Min(value = 0, message = "from should not be less than 0") Integer from,
            @RequestParam(required = false) @Min(value = 0, message = "size should not be less than 1") Integer size,
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @RequestHeader("X-Sharer-User-Id") long userId) {

        return bookingService.getBookingByStateAndOwner(from, size, userId, state); //

    }

}
