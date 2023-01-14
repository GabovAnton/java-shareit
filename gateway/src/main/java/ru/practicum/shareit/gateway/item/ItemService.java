package ru.practicum.shareit.gateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.gateway.item.dto.CommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.item.dto.ItemPatchDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"items"})
public class ItemService {

    private final ItemFeignClient itemFeignClient;

    @Cacheable(value = "items", key = "{#userId + #itemId}", unless="#result.id != null")
    public ItemDto getItem(
            long itemId, long userId) {

        return itemFeignClient.getItem(itemId, userId);
    }

    @Cacheable(value = "items", key = "{#userId}")
    //@Cacheable(value = "items")
    public List<ItemDto> getAll(
            long userId, Integer from, Integer size) {

        return itemFeignClient.getAll(userId, from, size);
    }

    @CachePut(value = "items", key = "{#userId, #result.id}", unless="#result.lastBooking != null || #result" +
                                                                     ".nextBooking != null ")
    public ItemDto create(long userId, ItemDto itemDto) {

        return itemFeignClient.create(userId, itemDto);
    }

    //@CachePut(value = "items", key = "{#userId, #result.id}")
    @CachePut(value = "items", key = "{#userId, #result.id}", unless="#result.lastBooking != null || #result" +
                                                                     ".nextBooking != null ")
    //TODO добавить удаление кэша из поиска

/*    @Caching(
            cacheable = {
                    @Cacheable("users"),
                    @Cacheable("contacts")
            },
            put = {
                    @CachePut("tables"),
                    @CachePut("chairs"),
                    @CachePut(value = "meals", key = "#user.email")
            },
            evict = {
                    @CacheEvict(value = "services", key = "#user.name")
            }
    )*/
    public ItemDto update(
            long userId, long itemId, ItemPatchDto itemPatchDto) {

        return itemFeignClient.update(userId, itemId, itemPatchDto);
    }

    @Cacheable(value="search", key = "{ #userId + #text}")
    public List<ItemDto> search(
            long userId, Integer from, Integer size, String text) {

        return itemFeignClient.search(userId, from, size, text);
    }

/*
    @CacheEvict(key = "{ #userId + #itemId}")
*/
    //@CacheEvict(key = "{ #userId + #itemId}")
    @CacheEvict(value = "items", key = "{#userId, #itemId}")
    public CommentDto postComment(
            Long itemId, CommentDto commentDto, Long userId) {

        return itemFeignClient.postComment(itemId, commentDto, userId);
    }

}
