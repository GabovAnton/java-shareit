package ru.practicum.shareit.booking;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    Booking bookingDtoToBooking(BookingDto bookingDto);

    BookingDto bookingToBookingDto(Booking booking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking updateBookingFromBookingDto(BookingDto bookingDto, @MappingTarget Booking booking);

}
