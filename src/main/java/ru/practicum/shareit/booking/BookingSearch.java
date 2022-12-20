package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface BookingSearch {
 //   List<Booking> getBookings(int from, int size, long ownerId, BookingRepository bookingRepository);
    List<BookingDto> getBookings(int from, int size, long ownerId);


    List<BookingDto> getBookingsByItemsOwner(int from, int size, long ownerId);
}

@RequiredArgsConstructor
class SearchAll extends BookingSearchRoot implements BookingSearch  {


    @Override
    public List<BookingDto> getBookings(int from, int size, long ownerId) {


        //    @Query("select b from Booking b where b.booker.id = ?1 order by b.start DESC")
        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(int from, int size, long ownerId) {

        //    @Query("select b from Booking b where   b.item.owner.id = ?1 order by b.start DESC")


        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchCurrent extends BookingSearchRoot implements BookingSearch {

    @Override
    public List<BookingDto> getBookings(int from, int size, long ownerId ) {

       // @Query("select b from Booking b where b.booker.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start DESC")
        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId)
                        .and(request.start.before(currentTime)
                                .and(request.end.after(currentTime))))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(int from, int size, long ownerId ) {

        //    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start <?2 and b.end > ?2  order by b.start DESC")

        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId)
                        .and(request.start.before(currentTime)
                            .and(request.end.after(currentTime))))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchPast extends BookingSearchRoot  implements BookingSearch {

    @Override
    public List<BookingDto> getBookings(int from, int size, long ownerId ) {
        // @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2 order by b.start DESC")
        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId)
                        .and(request.end.before(currentTime)))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(int from, int size, long ownerId ) {
        //    @Query("select b from Booking b where  b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId)
                        .and(request.end.before(currentTime)))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchFuture extends BookingSearchRoot  implements BookingSearch {


    @Override
    public List<BookingDto> getBookings(int from, int size, long ownerId) {
        //    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2 order by b.start DESC")

        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId)
                        .and(request.start.after(currentTime))
                        .and(request.end.after(currentTime)))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(int from, int size, long ownerId) {
        //    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start >?2 and b.end > ?2  order by b.start DESC")

        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId)
                        .and(request.start.after(currentTime))
                        .and(request.end.after(currentTime)))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchWaiting extends BookingSearchRoot  implements BookingSearch {


    @Override
    public List<BookingDto> getBookings(int from, int size, long ownerId) {
        //    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start DESC")

        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId).and(request.status.eq(BookingStatus.WAITING)))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(int from, int size, long ownerId) {
//    @Query("select b from Booking b " +
//            "where b.item.owner.id = ?1 and b.status = ?2 " +
//            "order by b.start DESC")

        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId).and(request.status.eq(BookingStatus.WAITING)))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchRejected extends BookingSearchRoot  implements BookingSearch {

    @Override
    public List<BookingDto> getBookings(int from, int size, long ownerId) {
        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId).and(request.status.eq(BookingStatus.REJECTED)))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(int from, int size, long ownerId) {


        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId).and(request.status.eq(BookingStatus.REJECTED)))
                .orderBy(request.start.desc())
                .limit(size)
                .offset(--from)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
}



