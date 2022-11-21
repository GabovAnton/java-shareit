package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestDao;

public class ItemMapper {

    @Autowired
private  final ItemRequestDao requestDao;

    public ItemMapper(ItemRequestDao requestDao) {
        this.requestDao = requestDao;
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                itemDto.getOwner(),
                itemDto.getRequestId() != null ?  requestDao.get(itemDto.getRequestId()) : null
        );
    }

}
