package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

public abstract class BookingMapperDecorator implements BookingMapper {
    @Autowired
    protected ItemService itemService;
    @Autowired
    protected UserService userService;
    @Autowired
    @Qualifier("delegate")
    private BookingMapper delegate;

    @Override
    public Booking bookingDtoToBooking(BookingCreateDto bookingCreateDto, long userId) {
        Booking booking = delegate.bookingDtoToBooking(bookingCreateDto, userId);
        booking.setBooker(userService.getUser(bookingCreateDto.getUserId()));

        return booking;
    }

}
