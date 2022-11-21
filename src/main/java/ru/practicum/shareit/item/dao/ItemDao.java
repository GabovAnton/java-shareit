package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemDao {
    private List<Item> itemList = new ArrayList<>();

    public List<Item> getALL() {
        return null;
    }

    public Item get(int id) {
        return null;
    }

    public void save(Item item) {
    }
}
