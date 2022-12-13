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
        List<Booking> bookings = bookingRepository.findByBooker_Id(ownerId);
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {

        return  bookingRepository.findByItem_Owner_IdOrderByStartDesc(ownerId);
    }
}

class SearchCurrent implements BookingSearch {

    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByBooker_IdCurrent(ownerId, LocalDateTime.now());
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByItem_Owner_IdCurrent(ownerId, LocalDateTime.now());
    }
}

class SearchPast implements BookingSearch {

    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {

        return bookingRepository
                .findByBooker_IdAndEndIsBefore(ownerId, LocalDateTime.now());
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByItem_Owner_IdInPastOrderByStartDesc(ownerId, LocalDateTime.now());
    }
}

class SearchFuture implements BookingSearch {


    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByBooker_IdAndStartIsAfter(ownerId, LocalDateTime.now());
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByItem_Owner_IdInFutureOrderByStartDesc(ownerId, LocalDateTime.now());
    }
}

class SearchWaiting implements BookingSearch {


    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByBooker_IdAndStatus(ownerId, BookingStatus.WAITING);
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
    }
}

class SearchRejected implements BookingSearch {

    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByBooker_IdAndStatus(ownerId, BookingStatus.REJECTED);
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
    }
}



