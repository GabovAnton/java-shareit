package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemDao {
    private List<Item> itemList = new ArrayList<>();

    public List<Item> getALL() {
        return itemList;
    }

    public Optional<Item> get(int id) {
        return itemList.stream().filter(x->x.getId() == id).findFirst();
    }

    public  boolean save (Item item) {
        if (itemList.stream().anyMatch(x -> x.equals(item))) return false;
        itemList.add(item);
        return true;

    }

    public List<Item> search(String query) {
        //TODO insert correct algorithm
       return itemList.
               stream().
               filter(x->x.getName().contains(query)  || x.getDescription().contains(query)).
               collect(Collectors.toList());

    }

}
