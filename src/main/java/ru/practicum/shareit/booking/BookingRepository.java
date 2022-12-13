package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start DESC")
    List<Booking> findByBooker_IdAndStatus(@NonNull Long id, @NonNull BookingStatus status);

    @Query("select b from Booking b where b.booker.id = ?1 order by b.start DESC")
    List<Booking> findByBooker_Id(@NonNull Long id);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start DESC")
    List<Booking> findByBooker_IdCurrent(Long bookerId, LocalDateTime date);


    @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime date);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2 order by b.start DESC")
    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime date);

    @Query("select b from Booking b where   b.item.owner.id = ?1 order by b.start DESC")
    List<Booking> findByItem_Owner_IdOrderByStartDesc(@NonNull Long id);

    @Query("select b from Booking b where  b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> findByItem_Owner_IdInPastOrderByStartDesc(@NonNull Long id, LocalDateTime date);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start >?2 and b.end > ?2  order by b.start DESC")
    List<Booking> findByItem_Owner_IdInFutureOrderByStartDesc(@NonNull Long id, LocalDateTime date);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start <?2 and b.end > ?2  order by b.start DESC")
    List<Booking> findByItem_Owner_IdCurrent(@NonNull Long id, LocalDateTime date);

    @Query("select b from Booking b where b.id = ?1 and b.booker.id = ?2 order by b.start DESC")
    List<Booking> findByIdAndBooker_Id(@NonNull Long itemId, @NonNull Long bookerId);


    boolean existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(@NonNull Long itemId,
                                                               @NonNull Long bookerId,
                                                               @NonNull BookingStatus status,
                                                               LocalDateTime end);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and b.status = ?2 " +
            "order by b.start DESC")
    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(@NonNull Long id,
                                                               @NonNull BookingStatus status);


}