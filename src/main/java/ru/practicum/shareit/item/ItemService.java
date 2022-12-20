package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    ItemDto getItemDto(long id, long userId);

    List<ItemDto> getAll(int from, int size, long userId);

    Item map(long id);

    Item save(Item item, long userId);

    Comment saveComment(Long itemId, Long userId, CommentDto commentDto);

    List<ItemDto> search(int from, int size, String text);

    ItemDto update(ItemPatchDto itemDto, long userId);

    Boolean isItemAvailable(long itemId);

}
