package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item getItem(long id);

    List<ItemDto> getAll(long userId);

    Long save(Item item, long userId);

    List<ItemDto> search(String text, long userId);

    ItemDto update(ItemPatchDto itemDto, long userId);


}
