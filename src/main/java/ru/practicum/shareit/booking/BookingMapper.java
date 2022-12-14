package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        uses = {ItemService.class, UserService.class})
public interface BookingMapper {

    @Mapping(source = "userId", target = "booker")
    @Mapping(source = "bookingCreateDto.itemId", target = "item")
    Booking bookingDtoToBooking(BookingCreateDto bookingCreateDto, long userId);

    BookingCreateDto bookingToBookingCreateDto(Booking booking);

    BookingDto bookingToBookingDto(Booking booking);


}
