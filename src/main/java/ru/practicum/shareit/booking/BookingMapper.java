package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {ItemService.class, UserService.class})
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "userId", target = "booker")
    @Mapping(source = "bookingCreateDto.itemId", target = "item")
    Booking bookingCreateDtoToBooking(BookingCreateDto bookingCreateDto, long userId);

    BookingDto bookingToBookingDto(Booking booking);

}
