package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("{itemId}")
    public Optional<Item> getItemById(@PathVariable int id) {
        Optional<Item> item = itemService.getItem(id);

        return item;
    }

    @GetMapping()
    public List<Item> getAll() {
        List<Item> all = itemService.getAll();
        return all;
    }

    @PostMapping()
    public Item create(@Valid @RequestBody Item newItem) {
        if (itemService.save(newItem)) {
           return newItem;
        } else {

            return newItem; //TODO temporary solution
        }
    }

    @PatchMapping()
    public Item update(@Valid @RequestBody ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        if (itemService.save(item)) {
            return item;
        } else {

            return item; //TODO temporary solution
        }
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Item> searchByQuery(@RequestParam String text) {
        return itemService.search(text);
    }



}
