package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingSearch {

 //   List<Booking> getBookings(long ownerId);

    List<Booking> getBookings(long ownerId, BookingRepository bookingRepository);

 //   List<Booking> getBookingsByItemsOwner(long ownerId);

  List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository);
}
class All implements BookingSearch {



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

class Current implements BookingSearch {


    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByBooker_IdAndEndIsAfter(ownerId, LocalDateTime.now());
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByItem_Owner_IdInFutureOrderByStartDesc(ownerId, LocalDateTime.now());
    }
}

class Past implements BookingSearch {

    @Override
    public List<Booking> getBookings(long ownerId, BookingRepository bookingRepository) {

        return bookingRepository
                .findByBooker_IdAndEndIsBefore(ownerId, LocalDateTime.now());
        //,Sort.by(Sort.Direction.DESC, "start_date"));
    }

    @Override
    public List<Booking> getBookingsByItemsOwner(long ownerId, BookingRepository bookingRepository) {
        return bookingRepository
                .findByItem_Owner_IdInPastOrderByStartDesc(ownerId, LocalDateTime.now());
    }
}

class Future implements BookingSearch {


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

class Waiting implements BookingSearch {


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

class Rejected implements BookingSearch {

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



