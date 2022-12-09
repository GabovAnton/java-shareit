package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

public abstract class BookingMapperDecorator implements BookingMapper{
    @Autowired
    @Qualifier("delegate")
    private BookingMapper delegate;

    @Autowired
    protected  ItemService itemService;

    @Autowired
    protected UserService userService;

    @Override
    public Booking bookingDtoToBooking(BookingCreateDto bookingCreateDto, long userId ) {
        Booking booking = delegate.bookingDtoToBooking(bookingCreateDto,  userId);
        booking.setBooker(userService.getUser(bookingCreateDto.getUserId()));
        booking.setItem(itemService.getItem(bookingCreateDto.getItemId()));

        return booking;
    }

}
