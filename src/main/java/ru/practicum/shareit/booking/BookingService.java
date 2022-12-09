package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    List<BookingCreateDto> findAll(Long id);
    Booking save(Booking booking);
    Booking changeBookingStatus(long bookingId, Boolean isApproved, long ownerId);
    Booking getBooking(long requesterId, long bookingId);
    List<BookingDto> getBookingByState(long ownerId, String state);

    List<BookingDto> getBookingByStateAndOwner(long ownerId, String state);
}

