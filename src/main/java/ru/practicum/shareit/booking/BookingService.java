package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking save(Booking booking, long userId);

    Booking changeBookingStatus(long bookingId, Boolean isApproved, long ownerId);

    Booking getBooking(long requesterId, long bookingId);

    List<BookingDto> getBookingByState(int from, int size, long ownerId, String state);

    List<BookingDto> getBookingByStateAndOwner(int from, int size, long ownerId, String state);
}

