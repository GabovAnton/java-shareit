package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item getItem(long id);

    List<ItemDto> getAll(long userId);

    Item save(Item item, long userId);

    List<ItemDto> search(String text );

    ItemDto update(ItemPatchDto itemDto, long userId);
    Boolean isItemAvailable(long itemId);


}
