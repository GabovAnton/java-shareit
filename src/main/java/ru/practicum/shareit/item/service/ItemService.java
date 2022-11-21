package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Service
public interface ItemService {
    Optional<Item> getItem(int id);
    List<Item> getAll();

    boolean save(Item item);
}
