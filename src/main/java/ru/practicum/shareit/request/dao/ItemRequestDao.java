package ru.practicum.shareit.request.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRequestDao {
    private List<ItemRequest> itemRequestList = new ArrayList<>();

    public ItemRequest get(Integer id){
        return null;
    }

}
