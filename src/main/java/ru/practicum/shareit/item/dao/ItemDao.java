package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemDao {
    private final List<Item> itemList = new ArrayList<>();
    private long itemId;

    public List<Item> getAll(long id) {
        return itemList.stream().filter(x -> x.getOwner().getId() == id).collect(Collectors.toList());
    }

    public Optional<Item> get(long id) {
        return itemList.stream().filter(x -> x.getId() == id).findFirst();
    }


    public boolean save(Item item) {
        item.setId(++itemId);
        if (itemList.stream().anyMatch(x -> x.equals(item))) return false;
        itemList.add(item);
        return true;

    }

    public List<Item> search(String query, long userId) {
        return itemList.
                stream().
                filter(Item::isAvailable).
                filter(x -> x.getName().toLowerCase().contains(query) ||
                        x.getDescription().toLowerCase().contains(query) &&
                                x.getOwner().getId() != userId).
                collect(Collectors.toList());

    }

}
