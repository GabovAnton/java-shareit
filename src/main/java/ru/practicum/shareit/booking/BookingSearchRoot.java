package ru.practicum.shareit.booking;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public  class BookingSearchRoot {
/*    @PersistenceContext
    private final  EntityManager em;

    @Autowired
    BookingMapper bookingMapper;

    JPAQueryFactory queryFactory = new JPAQueryFactory(em);*/




    LocalDateTime currentTime = LocalDateTime.now();


}
