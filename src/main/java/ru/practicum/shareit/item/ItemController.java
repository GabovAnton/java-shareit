package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return ItemMapper.toItemDto(itemService.getItem(itemId));
    }

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping()
    public List<ItemDto> getAll(@RequestHeader("X-Later-User-Id") long userId) {
        return itemService.getAll(userId);
    }

    @JsonView(ItemDto.SimpleView.class)
    @PostMapping()
    public ItemDto create(@Validated(ItemDto.New.class) @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Later-User-Id") long userId) {
        long id = itemService.save(ItemMapper.toItem(itemDto), userId);
        return ItemMapper.toItemDto(itemService.getItem(id));
    }

    @JsonView(ItemDto.SimpleView.class)
    @PatchMapping()
    public ItemDto update(@Validated(ItemDto.Update.class) @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Later-User-Id") long userId) {
        return itemService.update(itemDto, userId);
    }

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping("/search")
    @ResponseBody
    public List<Item> searchByQuery(@RequestParam String text, @RequestHeader("X-Later-User-Id") long userId) {
        return itemService.search(text, userId);
    }


}
