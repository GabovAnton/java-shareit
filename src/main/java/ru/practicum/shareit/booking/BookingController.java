package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        /* return ItemMapper.toItemDto(itemService.getItem(itemId));*/
        return null;//TODO

    }

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping()
    public List<BookingDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.findAll(userId);
       // return null;//TODO
    }
}
