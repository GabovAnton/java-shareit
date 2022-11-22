package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item getItem(long id);
    List<ItemDto> getAll(long userId);

    Long save(Item item, long userId);

    List<Item> search(String text, long userId);
    ItemDto update(ItemDto itemDto, long userId);


}
