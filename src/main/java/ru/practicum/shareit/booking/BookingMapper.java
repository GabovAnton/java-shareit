package ru.practicum.shareit.booking;

import org.mapstruct.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        uses = {ItemService.class, UserService.class})
//@DecoratedWith(BookingMapperDecorator.class)
public interface BookingMapper {

    @Mapping(source = "userId", target = "booker")
    @Mapping(source = "bookingCreateDto.itemId", target = "item")
    Booking bookingDtoToBooking(BookingCreateDto bookingCreateDto, long userId);//, @Context ItemService service);

    BookingCreateDto bookingToBookingCreateDto(Booking booking);

    // Booking bookingDtoToBooking(BookingDto bookingDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking updateBookingFromBookingDto(BookingCreateDto bookingCreateDto, @MappingTarget Booking booking);


    BookingDto bookingToBookingDto(Booking booking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking updateBookingFromBookingDto(BookingDto bookingDto, @MappingTarget Booking booking);
}
