package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.request.ItemRequestDao;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ItemMapper {
   ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    Item itemDtoToItem(ItemDto itemDto);

    ItemDto itemToItemDto(Item item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item updateItemFromItemDto(ItemPatchDto itemPatchDto, @MappingTarget Item item);



}
