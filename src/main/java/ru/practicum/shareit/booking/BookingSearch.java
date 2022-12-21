package ru.practicum.shareit.booking;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface BookingSearch {
 //   List<Booking> getBookings(int from, int size, long ownerId, BookingRepository bookingRepository);
    List<BookingDto> getBookings(Integer from, Integer size, long ownerId, EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper);

    List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId, EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper);
}

@RequiredArgsConstructor
@Service
class SearchAll extends BookingSearchRoot implements BookingSearch  {
    @Autowired
    BookingRepository bookingRepository;


    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId, EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper) {
        QBooking request = QBooking.booking;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        //    @Query("select b from Booking b where b.booker.id = ?1 order by b.start DESC")
        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper) {

        //    @Query("select b from Booking b where   b.item.owner.id = ?1 order by b.start DESC")
        QBooking request = QBooking.booking;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchCurrent extends BookingSearchRoot implements BookingSearch {

    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper) {

        QBooking request = QBooking.booking;
       // @Query("select b from Booking b where b.booker.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start DESC")
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId)
                        .and(request.start.before(currentTime)
                                .and(request.end.after(currentTime))))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper ) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        //    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start <?2 and b.end > ?2  order by b.start DESC")
        QBooking request = QBooking.booking;

        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId)
                        .and(request.start.before(currentTime)
                            .and(request.end.after(currentTime))))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchPast extends BookingSearchRoot  implements BookingSearch {


    @Autowired
    BookingMapper bookingMapper;


    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager ,BookingRepository bookingRepository,BookingMapper bookingMapper) {
        // @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2 order by b.start DESC")
        QBooking request = QBooking.booking;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId)
                        .and(request.end.before(currentTime)))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper ) {
        QBooking request = QBooking.booking;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        //    @Query("select b from Booking b where  b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId)
                        .and(request.end.before(currentTime)))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchFuture extends BookingSearchRoot  implements BookingSearch {

    @Autowired
    BookingMapper bookingMapper;


    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper) {
        QBooking request = QBooking.booking;

        //    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2 order by b.start DESC")
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId)
                        .and(request.start.after(currentTime))
                        .and(request.end.after(currentTime)))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper) {
        QBooking request = QBooking.booking;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        //    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start >?2 and b.end > ?2  order by b.start DESC")

        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId)
                        .and(request.start.after(currentTime))
                        .and(request.end.after(currentTime)))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchWaiting extends BookingSearchRoot  implements BookingSearch {

    @Autowired
    BookingMapper bookingMapper;


    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper) {
        QBooking request = QBooking.booking;

        //    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start DESC")
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId).and(request.status.eq(BookingStatus.WAITING)))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper) {
//    @Query("select b from Booking b " +
//            "where b.item.owner.id = ?1 and b.status = ?2 " +
//            "order by b.start DESC")
        QBooking request = QBooking.booking;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId).and(request.status.eq(BookingStatus.WAITING)))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchRejected extends BookingSearchRoot  implements BookingSearch {

    @Autowired
    BookingMapper bookingMapper;

    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper) {
        QBooking request = QBooking.booking;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(request)
                .where(request.booker.id.eq(ownerId).and(request.status.eq(BookingStatus.REJECTED)))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository,BookingMapper bookingMapper) {
        QBooking request = QBooking.booking;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId).and(request.status.eq(BookingStatus.REJECTED)))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
}



