package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Primary
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;


    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserService userService,
                              ItemService itemService,
                              BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public List<BookingCreateDto> findAll(Long userId) {//TODO useriD ??

        List<BookingCreateDto> bookingCreateDto = bookingRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(bookingMapper::bookingToBookingCreateDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        return bookingCreateDto;
    }

    @Override
    public Booking save(Booking booking) {
        checkBookingBasicConstraints(booking);
        booking.setStatus(BookingStatus.WAITING);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking changeBookingStatus(long bookingId, Boolean isApproved, long requesterId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId + " not found"));

        if (!booking.getBooker().getId().equals(requesterId)) {
            throw new ForbiddenException("Booking status could be changed only by owner");
        }
        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking getBooking(long requesterId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId + " not found"));
        if (booking.getBooker().getId().equals(requesterId) ||
                booking.getItem().getOwner().getId().equals(requesterId)) {
            return booking;
        } else {
            throw new ForbiddenException("Booking could be retrieved only by items owner or booking author");
        }
    }

    @Override
    public List<BookingDto> getBookingByState(long ownerId, String state) {
        BookingSearch bookingSearch = BookingSearchFactory
                .getSearchMethod(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid search method"));

      return   bookingSearch.getBookings(ownerId, bookingRepository).stream()
                .filter(Objects::nonNull)
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
    @Override
    public List<BookingDto> getBookingByStateAndOwner(long ownerId, String state) {
        BookingSearch bookingSearch = BookingSearchFactory
                .getSearchMethod(state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid search method"));

        return   bookingSearch.getBookingsByItemsOwner(ownerId, bookingRepository).stream()
                .filter(Objects::nonNull)
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }



    private void checkBookingBasicConstraints(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().isBefore(LocalDateTime.now())
                || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ForbiddenException("Booking start should be less than End and not be in past");
        }
        itemService.isItemAvailable(booking.getItem().getId());
    }


}
