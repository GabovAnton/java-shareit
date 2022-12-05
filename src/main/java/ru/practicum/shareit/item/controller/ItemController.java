package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
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
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
/*        long id = itemService.save(ItemMapper.toItem(itemDto), userId);
        return ItemMapper.toItemDto(itemService.getItem(id));*/
        return  null;
    }


    @JsonView(ItemDto.SimpleView.class)
    @PatchMapping("{itemId}")
    public ItemDto update(@PathVariable long itemId, @Valid @RequestBody ItemPatchDto itemPatchDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        itemPatchDto.setId(itemId);
        return itemService.update(itemPatchDto, userId);
    }

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping("/search")
    @ResponseBody
    public List<ItemDto> searchByQuery(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") long userId) {

        List<ItemDto> search = (itemService.search(text, userId));
        return search;
    }


}
