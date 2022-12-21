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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface BookingSearch {
 //   List<Booking> getBookings(int from, int size, long ownerId, BookingRepository bookingRepository);
    List<BookingDto> getBookings(Integer from, Integer size, long ownerId, EntityManager entityManager,BookingRepository bookingRepository);

    List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId, EntityManager entityManager,BookingRepository bookingRepository);
}

@Service
class SearchAll extends BookingSearchRoot implements BookingSearch  {

    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId, EntityManager entityManager,
                                        BookingRepository bookingRepository) {


        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        //    @Query("select b from Booking b where b.booker.id = ?1 order by b.start DESC")
        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId))
                .orderBy(qBooking.start.desc()).limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
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

        return queryFactory.selectFrom(request)
                .where(request.item.owner.id.eq(ownerId))
                .orderBy(request.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchCurrent extends BookingSearchRoot implements BookingSearch {


    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {


       // @Query("select b from Booking b where b.booker.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);

        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId)
                        .and(qBooking.start.before(currentTime)
                                .and(qBooking.end.after(currentTime))))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository ) {

        //    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start <?2 and b.end > ?2  order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);

        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId)
                        .and(qBooking.start.before(currentTime)
                            .and(qBooking.end.after(currentTime))))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchPast extends BookingSearchRoot  implements BookingSearch {



    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
        // @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);

        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId)
                        .and(qBooking.end.before(currentTime)))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository ) {


        //    @Query("select b from Booking b where  b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId)
                        .and(qBooking.end.before(currentTime)))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchFuture extends BookingSearchRoot  implements BookingSearch {



    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {

        //    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId)
                        .and(qBooking.start.after(currentTime))
                        .and(qBooking.end.after(currentTime)))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {

        //    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start >?2 and b.end > ?2  order by b.start DESC")

        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId)
                        .and(qBooking.start.after(currentTime))
                        .and(qBooking.end.after(currentTime)))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchWaiting extends BookingSearchRoot  implements BookingSearch {



    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {

        //    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId).and(qBooking.status.eq(BookingStatus.WAITING)))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
//    @Query("select b from Booking b " +
//            "where b.item.owner.id = ?1 and b.status = ?2 " +
//            "order by b.start DESC")
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId).and(qBooking.status.eq(BookingStatus.WAITING)))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }
}

class SearchRejected extends BookingSearchRoot  implements BookingSearch {



    @Override
    public List<BookingDto> getBookings(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.booker.id.eq(ownerId).and(qBooking.status.eq(BookingStatus.REJECTED)))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Integer from, Integer size, long ownerId,EntityManager entityManager,BookingRepository bookingRepository) {
        QBooking qBooking = QBooking.booking;
        JPAQuery<Booking> query = new JPAQuery<>(entityManager);
        return query.from(qBooking)
                .where(qBooking.item.owner.id.eq(ownerId).and(qBooking.status.eq(BookingStatus.REJECTED)))
                .orderBy(qBooking.start.desc())
                .limit( (size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0)
                .fetch().stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
}



