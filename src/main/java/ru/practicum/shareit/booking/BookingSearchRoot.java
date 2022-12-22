package ru.practicum.shareit.booking;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BookingSearchRoot {
    /*    @PersistenceContext
        private final  EntityManager em;

        @Autowired
        BookingMapper bookingMapper;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);*/



    public List<Booking> addLimitAndOffset(Integer from, Integer size, JPAQuery<Booking> query,
                                           BookingRepository bookingRepository) {
        return query.limit((size != null ? size : bookingRepository.count()))
                .offset(from != null ? --from : 0).fetch();
    }
}
