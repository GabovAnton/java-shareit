package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_IdAndStatus(@NonNull Long id, @NonNull BookingStatus status,Sort sort);

    List<Booking> findByBooker_Id(@NonNull Long id, Sort sort);
    List<Booking> findByBooker_IdAndEndIsAfter(Long bookerId, LocalDateTime date, Sort sort);


    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime date, Sort sort);
    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime date, Sort sort);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.owner.id = ?1 order by b.start DESC")
    List<Booking> findByBooker_IdAndItem_Owner_IdOrderByStartDesc(@NonNull Long id);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> findByBooker_IdAndItem_Owner_IdInPastOrderByStartDesc(@NonNull Long id,LocalDateTime date);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.owner.id = ?1 and b.end > ?2 order by b.start DESC")
    List<Booking> findByBooker_IdAndItem_Owner_IdInFutureOrderByStartDesc(@NonNull Long id,LocalDateTime date);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and b.item.owner.id = ?1 and b.status = ?2 " +
            "order by b.start DESC")
    List<Booking> findByBooker_IdAndItem_Owner_IdAndStatusOrderByStartDesc(@NonNull Long id,
                                                                           @NonNull BookingStatus status);

    List<Booking> findByItem_IdAndItem_Owner_Id(@NonNull Long itemId, @NonNull Long ownerId);

    List<Booking> findByItem_Owner_IdAndItem_IdOrderByStartDesc(@NonNull Long ownerId, @NonNull Long itemId, Pageable pageable);




}