package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.Request;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start DESC")
    List<Booking> SearchBookingsByBookerAndStatus(@NonNull Long id, @NonNull BookingStatus status);

    @Query("select b from Booking b where b.booker.id = ?1 order by b.start DESC")
    List<Booking> SearchBookingsByBooker(@NonNull Long id);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start DESC")
    List<Booking> SearchBookingsByBookerInPresentTime(Long bookerId, LocalDateTime date);


    @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> SearchBookingsByBookerInPastTime(Long bookerId, LocalDateTime date);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2 order by b.start DESC")
    List<Booking> SearchBookingsByBookerInFutureTime(Long bookerId, LocalDateTime date);

    @Query("select b from Booking b where   b.item.owner.id = ?1 order by b.start DESC")
    List<Booking> SearchBookingsByItemOwner(@NonNull Long id);

    @Query("select b from Booking b where  b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> SearchBookingsByItemOwnerInPastTime(@NonNull Long id, LocalDateTime date);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start >?2 and b.end > ?2  order by b.start DESC")
    List<Booking> SearchBookingsByItemOwnerInFutureTime(@NonNull Long id, LocalDateTime date);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start <?2 and b.end > ?2  order by b.start DESC")
    List<Booking> SearchBookingsByItemOwnerInPresentTime(@NonNull Long id, LocalDateTime date);

    @Query("select b from Booking b where b.id = ?1 and b.booker.id = ?2 order by b.start DESC")
    List<Booking> SearchBookingsById(@NonNull Long itemId, @NonNull Long bookerId);


    boolean existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(@NonNull Long itemId,
                                                               @NonNull Long bookerId,
                                                               @NonNull BookingStatus status,
                                                               LocalDateTime end);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and b.status = ?2 " +
            "order by b.start DESC")
    List<Booking> SearchBookingsByItemOwnerAndStatus(@NonNull Long id,
                                                     @NonNull BookingStatus status);


}