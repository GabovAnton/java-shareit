package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    @Autowired
    public BookingController(BookingService bookingService, BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }


    @PostMapping()
    public BookingDto create(@Valid @RequestBody BookingCreateDto bookingCreateDto, @RequestHeader("X-Sharer-User-Id") long userId) {

        Booking booking = bookingMapper.bookingDtoToBooking(bookingCreateDto, userId);
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
    public BookingDto getBookingById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        Booking booking = bookingService.getBooking(userId, bookingId);

        BookingDto bookingDto = bookingMapper.bookingToBookingDto(booking);
        return bookingDto;

    }

    @GetMapping("")
    public List<BookingDto> getBookingByState(@RequestParam(defaultValue = "ALL", required = false) String state, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingByState(userId, state);

    }

    @GetMapping("/owner")
    public List<BookingDto> getItemsByStateAndOwner(@RequestParam(defaultValue = "ALL", required = false) String state, @RequestHeader("X-Sharer-User-Id") long userId) {

        List<BookingDto> bookingByStateAndOwner = bookingService.getBookingByStateAndOwner(userId, state);
        return bookingByStateAndOwner; //

    }

}
