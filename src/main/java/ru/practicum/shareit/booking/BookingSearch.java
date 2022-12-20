package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingSearch {
    List<Booking> getBookings(long ownerId, BookingRepository bookingRepository);

    List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository);
}

class SearchAll implements BookingSearch {


    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByBooker(ownerId);
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {

        return bookingRepository.SearchBookingsByItemOwner(ownerId);
    }
}

class SearchCurrent implements BookingSearch {

    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByBookerInPresentTime(ownerId, LocalDateTime.now());
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByItemOwnerInPresentTime(ownerId, LocalDateTime.now());
    }
}

class SearchPast implements BookingSearch {

    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {

        return bookingRepository.SearchBookingsByBookerInPastTime(ownerId, LocalDateTime.now());
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByItemOwnerInPastTime(ownerId, LocalDateTime.now());
    }
}

class SearchFuture implements BookingSearch {


    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByBookerInFutureTime(ownerId, LocalDateTime.now());
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByItemOwnerInFutureTime(ownerId, LocalDateTime.now());
    }
}

class SearchWaiting implements BookingSearch {


    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByBookerAndStatus(ownerId, BookingStatus.WAITING);
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByItemOwnerAndStatus(ownerId, BookingStatus.WAITING);
    }
}

class SearchRejected implements BookingSearch {

    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByBookerAndStatus(ownerId, BookingStatus.REJECTED);
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository.SearchBookingsByItemOwnerAndStatus(ownerId, BookingStatus.REJECTED);
    }
}



