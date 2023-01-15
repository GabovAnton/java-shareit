package ru.practicum.shareit.gateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.gateway.item.dto.CommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.item.dto.ItemPatchDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemFeignClient itemFeignClient;

    @Autowired
    private TestServiceEvictCache testServiceEvictCache;

    @Cacheable(value = "items", key = "{#userId, #itemId}", unless = "#result.id != null")
    public ItemDto getItem(
            long itemId, long userId) {

        return itemFeignClient.getItem(itemId, userId);
    }

    @Cacheable(value = "items", key = "{#userId}")
    public List<ItemDto> getAll(
            long userId, Integer from, Integer size) {

        return itemFeignClient.getAll(userId, from, size);
    }

    @CachePut(value = "items", key = "{#userId + #result.id}", unless = "#result.id != null")
    public ItemDto create(long userId, ItemDto itemDto) {

        testServiceEvictCache.evictAllCacheValues("search");

        return itemFeignClient.create(userId, itemDto);
    }

    @CachePut(value = "items", key = "{#userId + #result.id}", unless = "#result.id != null")
    public ItemDto update(
            Long userId, Long itemId, ItemPatchDto itemPatchDto) {

        testServiceEvictCache.evictAllCacheValues("search");
        return itemFeignClient.update(userId, itemId, itemPatchDto);
    }

    @Cacheable(value = "search")
    public List<ItemDto> search(
            long userId, Integer from, Integer size, String text) {

        return itemFeignClient.search(userId, from, size, text);
    }

    public CommentDto postComment(
            Long itemId, CommentDto commentDto, Long userId) {

        testServiceEvictCache.evictSingleCacheValue("items", userId.toString() + itemId.toString());

        return itemFeignClient.postComment(itemId, commentDto, userId);
    }

}
