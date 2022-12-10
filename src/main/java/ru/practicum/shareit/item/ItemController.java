package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @JsonView(ItemDto.SimpleView.class)
    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemDto(itemId, userId);

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
        Item savedItem = itemService.save(itemMapper.itemDtoToItem(itemDto), userId);
        return itemMapper.itemToItemDto(savedItem);
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

        List<ItemDto> search = (itemService.search(text));
        return search;
    }


}
