package ru.practicum.shareit.request.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.dao.Dao;
import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ItemRequestDao  {
    private final List<ItemRequest> itemRequestList = new ArrayList<>();

    public Optional<ItemRequest> get(long id) {
        return Optional.empty();
    }

    public List<ItemRequest> getAll(long id) {
        return null;
    }

    public boolean save(ItemRequest itemRequest) {
        return false;
    }

    public List<ItemRequest> search(String query,long userId) {
        return null;
    }
}
