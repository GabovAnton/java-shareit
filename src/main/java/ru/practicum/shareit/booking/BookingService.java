package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    List<BookingDto> findAll(Long id);
}
