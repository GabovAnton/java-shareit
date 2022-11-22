package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
//@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return ItemMapper.toItemDto(itemService.getItem(itemId));
    }

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping()
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAll(userId);
    }

    @JsonView(ItemDto.SimpleView.class)
    @PostMapping()
    public ItemDto create(@Validated(ItemDto.New.class) @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        long id = itemService.save(ItemMapper.toItem(itemDto), userId);
        return ItemMapper.toItemDto(itemService.getItem(id));
    }

    @JsonView(ItemDto.SimpleView.class)
    @PatchMapping()
    public ItemDto update(@Validated(ItemDto.Update.class) @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.update(itemDto, userId);
    }

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping("/search")
    @ResponseBody
    public List<Item> searchByQuery(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.search(text, userId);
    }


}
