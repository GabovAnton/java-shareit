package ru.practicum.shareit.booking;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public  class BookingSearchRoot {
    @Autowired
    EntityManager entityManager;
    @Autowired
    BookingMapper bookingMapper;

    JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

    QBooking request = QBooking.booking;

    @Autowired
    BookingRepository bookingRepository;

    LocalDateTime currentTime = LocalDateTime.now();

}
