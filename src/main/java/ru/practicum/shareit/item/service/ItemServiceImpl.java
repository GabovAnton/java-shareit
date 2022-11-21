package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;

    public ItemServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public Optional<Item> getItem(int id) {
        Optional<Item> item = Optional.ofNullable(itemDao.get(id));
        log.debug("item with id: {} requested, returned result: {}", id, item);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("item with id: " + id + " doesn't exists");
        }
        return item;
    }

    public List<Item> getAll() {
        List<Item> itemList = itemDao.getALL().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        log.debug("all items requested: {}", itemList.size());

        return itemList;

    }

    @Override
    public boolean save(Item item) {
        validate(item);
        itemDao.save(item);
        log.debug("new item created: {}", item);
        return true;
    }

    //TODO realize this!!!!
    private void validate(Item item) {
        // Details omitted
    }
}
