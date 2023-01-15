package ru.practicum.shareit.gateway.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.gateway.booking.dto.BookingCreateDto;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;
import ru.practicum.shareit.gateway.booking.dto.BookingState;
import ru.practicum.shareit.gateway.item.TestServiceEvictCache;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingFeignClient bookingFeignClient;

    @Autowired
    private TestServiceEvictCache testServiceEvictCache;

    public BookingDto create(Long userId, BookingCreateDto bookingCreateDto) {

        testServiceEvictCache.evictSingleCacheValue("items",
                userId.toString() + bookingCreateDto.getItemId().toString());
        testServiceEvictCache.evictAllCacheValues("bookings");

        return bookingFeignClient.create(userId, bookingCreateDto);
    }

    public BookingDto update(Long userId, Long bookingId, Boolean approved) {

        testServiceEvictCache.evictAllCacheValues("bookings");
        testServiceEvictCache.evictAllCacheValues("items");

        return bookingFeignClient.update(bookingId, approved, userId);

    }

    @Cacheable(value = "bookings", key = "{#bookingId }", unless = "#result.id != null")
    public BookingDto getBooking(long userId, long bookingId) {

        return bookingFeignClient.getBookingById(bookingId, userId);
    }

    public List<BookingDto> getBookingByState(Long userId, Integer from, Integer size, BookingState state) {

        return bookingFeignClient.getBookingByState(userId, from.toString(), size.toString(), state.name());

    }

    public List<BookingDto> getItemsByStateAndOwner(Long userId, Integer from, Integer size, BookingState state) {

        return bookingFeignClient.getItemsByStateAndOwner(from.toString(), size.toString(), state.name(), userId);

    }

}
