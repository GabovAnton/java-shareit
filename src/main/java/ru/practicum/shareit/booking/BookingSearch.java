package ru.practicum.shareit.booking;

import com.querydsl.core.support.QueryBase;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface BookingSearch {
 //   List<Booking> getBookings(int from, int size, long ownerId, BookingRepository bookingRepository);
    List<BookingDto> getBookings(Integer from, Integer size, long ownerId, EntityManager entityManager,BookingRepository bookingRepository);

    List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId, EntityManager entityManager,BookingRepository bookingRepository);
}

@Service
class SearchAll  implements BookingSearch  {

    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId, EntityManager entityManager,
                                        BookingRepository bookingRepository) {

        long TotalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        //    @Query("select b from Booking b where b.booker.id = ?1 order by b.start DESC")
        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : TotalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,
                                                    EntityManager entityManager,BookingRepository bookingRepository) {

        //    @Query("select b from Booking b where   b.item.owner.id = ?1 order by b.start DESC")
        QBooking request = QBooking.booking;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        long TotalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId))
                .orderBy(request.start.desc())
                .limit(size != null ? size : TotalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchCurrent  implements BookingSearch {


    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {


       // @Query("select b from Booking b where b.booker.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId)
                        .and(qBooking.start.before(LocalDateTime.now())
                                .and(qBooking.end.after(LocalDateTime.now()))))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,
                                                    EntityManager entityManager,BookingRepository bookingRepository ) {

        //    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start <?2 and b.end > ?2  order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;

        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId)
                        .and(qBooking.start.before(LocalDateTime.now())
                            .and(qBooking.end.after(LocalDateTime.now()))))
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .orderBy(qBooking.start.desc())
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchPast   implements BookingSearch {



    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
        // @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);

        long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId)
                        .and(qBooking.start.before(LocalDateTime.now()))
                        .and(qBooking.end.before(LocalDateTime.now())))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository ) {

        long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        //    @Query("select b from Booking b where  b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId)
                        .and(qBooking.end.before(LocalDateTime.now())))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchFuture   implements BookingSearch {



    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
        long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        //    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId)
                        .and(qBooking.start.after(LocalDateTime.now()))
                        .and(qBooking.end.after(LocalDateTime.now())))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {

        //    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start >?2 and b.end > ?2  order by b.start DESC")
        long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId)
                        .and(qBooking.start.after(LocalDateTime.now()))
                        .and(qBooking.end.after(LocalDateTime.now())))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchWaiting   implements BookingSearch {



    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
        long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        //    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId).and(qBooking.status.eq(BookingStatus.WAITING)))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
//    @Query("select b from Booking b " +
//            "where b.item.owner.id = ?1 and b.status = ?2 " +
//            "order by b.start DESC")

        long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId).and(qBooking.status.eq(BookingStatus.WAITING)))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}
/* class QueryHelper{
      static JPAQuery<Booking> query;
     public static JPAQuery<Booking> getQuery(EntityManager entityManager) {
         query = new JPAQuery<>(entityManager);
        return query;
    }

}*/
class SearchRejected   implements BookingSearch {
    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
        QBooking qBooking = QBooking.booking;
       JPAQuery<Booking> query = new JPAQuery<>(entityManager);

        //JPAQuery<Booking>   query =  QueryHelper.getQuery(entityManager);

            long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        List<BookingDto> collect = query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId).and(qBooking.status.eq(BookingStatus.REJECTED)))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        return collect;

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        long totalItems = bookingRepository.count() + 1;
        int offset = from != null ? from : 0;
        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId).and(qBooking.status.eq(BookingStatus.REJECTED)))
                .orderBy(qBooking.start.desc())
                .limit(size != null ? size : totalItems)
                .offset(offset)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
}



