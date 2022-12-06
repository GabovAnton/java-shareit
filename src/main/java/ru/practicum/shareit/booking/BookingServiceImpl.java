package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Primary
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<BookingDto> findAll(Long userId) {//TODO useriD ??
        List<Booking> all = bookingRepository.findAll();

        List<BookingDto> bookingDto = bookingRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        return bookingDto;
    }
}
