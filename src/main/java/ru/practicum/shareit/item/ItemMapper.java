package ru.practicum.shareit.item;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ItemMapper {

    Item itemDtoToItem(ItemDto itemDto);

    ItemDto itemToItemDto(Item item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item updateItemFromItemDto(ItemPatchDto itemPatchDto, @MappingTarget Item item);

}
