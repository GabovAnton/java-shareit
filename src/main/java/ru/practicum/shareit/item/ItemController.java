package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;


    @GetMapping("{itemId}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return ResponseEntity.ok(itemService.getItemDto(itemId, userId));

    }

    @GetMapping()
    public ResponseEntity<List<ItemDto>> getAll(@RequestParam(required = false)
                                                @Min(value = 0, message = "from should be positive") Integer from,
                                                @RequestParam(required = false)
                                                @Min(value = 0, message = "size should greater than 0") Integer size,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {

        return ResponseEntity.ok(itemService.getAll(from, size, userId));
    }

    @PostMapping()
    public ItemDto create(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        Item savedItem = itemService.save(ItemMapper.INSTANCE.itemDtoToItem(itemDto), userId);
        return ItemMapper.INSTANCE.itemToItemDto(savedItem);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@PathVariable long itemId, @Valid @RequestBody ItemPatchDto itemPatchDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        itemPatchDto.setId(itemId);
        return itemService.update(itemPatchDto, userId);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ItemDto> searchByQuery(@RequestParam(required = false)
                                       @Min(value = 0, message = "from should be positive") Integer from,
                                       @RequestParam(required = false)
                                       @Min(value = 0, message = "size should greater than 0") Integer size,
                                       @RequestParam String text, @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemService.search(from, size, text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto postComment(@PathVariable Long itemId, @Valid @RequestBody CommentDto commentDto,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        Comment comment = itemService.saveComment(itemId, userId, commentDto);
        CommentDto commentDto1 = CommentMapper.INSTANCE.commentToCommentDto(comment);
        return commentDto1;
    }


}
