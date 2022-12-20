package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private final ItemService itemService;
    @Autowired
    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemDto(itemId, userId);

    }

    @GetMapping()
    public List<ItemDto> getAll(@RequestParam(required = false) Integer from,
                                @RequestParam(required = false) Integer size,
                                @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemService.getAll(size, from, userId);
    }

    @PostMapping()
    public ItemDto create(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        Item savedItem = itemService.save(itemMapper.itemDtoToItem(itemDto), userId);
        return itemMapper.itemToItemDto(savedItem);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@PathVariable long itemId, @Valid @RequestBody ItemPatchDto itemPatchDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        itemPatchDto.setId(itemId);
        return itemService.update(itemPatchDto, userId);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ItemDto> searchByQuery(@PathVariable Integer from, @PathVariable Integer size, @RequestParam String text, @RequestHeader("X-Sharer-User-Id") long userId) {

        List<ItemDto> search = itemService.search(size, from, text);
        return search;
    }

    @PostMapping("{itemId}/comment")
    public CommentDto postComment(@PathVariable Long itemId, @Valid @RequestBody CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        Comment comment = itemService.saveComment(itemId, userId, commentDto);
        CommentDto commentDto1 = commentMapper.commentToCommentDto(comment);
        return commentDto1;
    }


}
