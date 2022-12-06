package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dao.ItemRequestDao;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    @Autowired
    private static ItemRequestDao requestDao;

/*    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }*/

/*    public static Item toItem(ItemDto itemDto) {

        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequestId() != null ? requestDao.get(itemDto.getRequestId()).orElseThrow() : null
        );
    }*/

}
