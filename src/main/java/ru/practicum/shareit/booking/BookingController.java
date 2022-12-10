package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemPatchDto;
import ru.practicum.shareit.item.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final ItemService itemService;

    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingController(BookingService bookingService,
                             ItemService itemService,
                             ItemMapper itemMapper,
                             BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
    }

 /*   @JsonView(ItemDto.SimpleView.class)
    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        *//* return ItemMapper.toItemDto(itemService.getItem(itemId));*//*
        return null;//TODO

    }*/

/*
    @JsonView(ItemDto.SimpleView.class)
    @GetMapping()
    public List<BookingCreateDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.findAll(userId);

    }
*/

    @PostMapping()
    public BookingDto create(@Valid @RequestBody BookingCreateDto bookingCreateDto,
                             @RequestHeader("X-Sharer-User-Id") long userId) {

        Booking booking = bookingMapper.bookingDtoToBooking(bookingCreateDto, userId);
        Booking savedBooking = bookingService.save(booking, userId);

        BookingDto bookingDto = bookingMapper.bookingToBookingDto(savedBooking);
        return bookingDto;
    }

    //  PATCH /bookings/{bookingId}?approved={approved}

    @PatchMapping("{bookingId}")
    public BookingDto update(@PathVariable long bookingId, @RequestParam Boolean approved,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        Booking booking = bookingService.changeBookingStatus(bookingId, approved, userId);
        BookingDto bookingDto = bookingMapper.bookingToBookingDto(booking);

        return bookingDto;
    }

    //GET /bookings/{bookingId}

    @GetMapping("{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        Booking booking = bookingService.getBooking(userId, bookingId);

        BookingDto bookingDto = bookingMapper.bookingToBookingDto(booking);
        return bookingDto;

    }

   // GET /bookings?state={state}
   @GetMapping("")
   public List<BookingDto> getBookingByState(@RequestParam(defaultValue = "ALL", required = false) String state,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
       return bookingService.getBookingByState(userId, state);

   }
   ///bookings/owner?state={state}
   @GetMapping("/owner")
   public List<BookingDto> getItemsByStateAndOwner(@RequestParam(defaultValue = "ALL", required = false) String state,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {

       List<BookingDto> bookingByStateAndOwner = bookingService.getBookingByStateAndOwner(userId, state);
       return bookingByStateAndOwner; //

   }

}
