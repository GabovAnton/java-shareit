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
    public Booking save(Booking booking, long userId) {

        checkBookingBasicConstraints(booking,userId);
        booking.setStatus(BookingStatus.WAITING);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking changeBookingStatus(long bookingId, Boolean isApproved, long requesterId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId + " not found"));

        if (!booking.getItem().getOwner().getId().equals(requesterId)) {
            throw new EntityNotFoundException("Booking status could be changed only by owner");
        }
        BookingStatus newStatus = isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        if (booking.getStatus().equals(newStatus)) {
            throw new ForbiddenException("Booking status has already been changed");
        } else {
            booking.setStatus(newStatus);
            bookingRepository.save(booking);
            return booking;
        }
    }

    @Override
    public Booking getBooking(long requesterId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId + " not found"));

        checkItemOwner(booking, requesterId);
        return booking;
    }

    @Override
    public List<BookingDto> getBookingByState(long ownerId, String state) {
        if (!userService.existsById(ownerId)) {
            throw new EntityNotFoundException("user with id: " + ownerId + " not found");
        } else {
            BookingSearch bookingSearch = BookingSearchFactory
                    .getSearchMethod(state)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS"));

            return bookingSearch.getBookings(ownerId, bookingRepository).stream()
                    .filter(Objects::nonNull)
                    .map(bookingMapper::bookingToBookingDto)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        }
    }

    @Override
    public List<BookingDto> getBookingByStateAndOwner(long ownerId, String state) {

        if (!userService.existsById(ownerId)) {
            throw new EntityNotFoundException("user with id: " + ownerId + " not found");
        } else {
            BookingSearch bookingSearch = BookingSearchFactory
                    .getSearchMethod(state)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS"));

            return bookingSearch.getBookingsByItemsOwner(ownerId, bookingRepository).stream()
                    .filter(Objects::nonNull)
                    .map(bookingMapper::bookingToBookingDto)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        }
    }


    private void checkItemOwner(Booking booking, Long requesterId) {
        if (!booking.getBooker().getId().equals(requesterId) &&
                !booking.getItem().getOwner().getId().equals(requesterId)) {
            throw new EntityNotFoundException("Booking could be retrieved only by items owner or booking author");
        }
    }

    private void checkBookingBasicConstraints(Booking booking, Long requesterId) {
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().isBefore(LocalDateTime.now())
                || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ForbiddenException("Booking start should be less than End and not be in past");
        }
        List<Booking> bookings = bookingRepository.findByIdAndBooker_Id(booking.getItem().getId(), requesterId);

        if (bookings.stream().anyMatch(b->b.getStatus().equals(BookingStatus.WAITING)
                || b.getStatus().equals(BookingStatus.APPROVED))) {
            throw  new EntityNotFoundException("Booking can't be made to one item more than one time");
        }



        itemService.isItemAvailable(booking.getItem().getId());
    }


}
