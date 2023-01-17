package ru.practicum.shareit.gateway.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.item.dto.CommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.item.dto.ItemPatchDto;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Get item {}, userId={}", itemId, userId);

        return itemService.getItem(itemId, userId);

    }

    @GetMapping()
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Getting items by userId={}, from={}, size={}", userId, from, size);

        return itemService.getAll(userId, from, size);
    }

    @PostMapping()
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Creating item {}, userId={}", itemDto, userId);

        return itemService.create(userId, itemDto);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@PathVariable long itemId,
            @RequestBody ItemPatchDto itemPatchDto,
            @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Updating item {}, userId={}, itemId={}", itemPatchDto, userId, itemId);

        return itemService.update(userId, itemId, itemPatchDto);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ItemDto> searchByQuery(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam String text) {

        log.info("Search item for userId={}, from={}, size={}, text={}", userId, from, size, text);

        return itemService.search(userId, from, size, text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto postComment(@PathVariable Long itemId,
            @RequestBody CommentDto commentDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Creating post comment {}, userId={}, itemId={}", commentDto, userId, itemId);

        return itemService.postComment(itemId, commentDto, userId);
    }

}
