package ru.practicum.shareit.gateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.gateway.item.dto.CommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.item.dto.ItemPatchDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class ItemService {

    private final ItemFeignClient itemFeignClient;

    private final ServiceEvictCache testServiceEvictCache;

    @Cacheable(value = "items", key = "{#userId, #itemId}", unless = "#result.id != null")
    public ItemDto getItem(long itemId, long userId) {

        return itemFeignClient.getItem(itemId, userId);
    }

    @Cacheable(value = "items", key = "{#userId}")
    public List<ItemDto> getAll(long userId, @PositiveOrZero Integer from, @Positive Integer size) {

        return itemFeignClient.getAll(userId, from, size);
    }

    @CachePut(value = "items", key = "{#userId + #result.id}", unless = "#result.id != null")
    public ItemDto create(long userId, @Valid ItemDto itemDto) {

        testServiceEvictCache.evictAllCacheValues("search");

        return itemFeignClient.create(userId, itemDto);
    }

    @CachePut(value = "items", key = "{#userId + #result.id}", unless = "#result.id != null")
    public ItemDto update(Long userId, Long itemId, @Valid ItemPatchDto itemPatchDto) {

        testServiceEvictCache.evictAllCacheValues("search");
        return itemFeignClient.update(userId, itemId, itemPatchDto);
    }

    @Cacheable(value = "search")
    public List<ItemDto> search(long userId, @PositiveOrZero Integer from, @Positive Integer size, String text) {

        return itemFeignClient.search(userId, from, size, text);
    }

    public CommentDto postComment(Long itemId, @Valid CommentDto commentDto, Long userId) {

        testServiceEvictCache.evictSingleCacheValue("items", userId.toString() + itemId.toString());

        return itemFeignClient.postComment(itemId, commentDto, userId);
    }

}
