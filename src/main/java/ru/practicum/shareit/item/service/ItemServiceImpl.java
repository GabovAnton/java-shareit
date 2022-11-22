package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.utils.ClassProperties;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final UserDao userDao;


    public Item getItem(long id) {
        Optional<Item> item = itemDao.get(id);
        log.debug("item with id: {} requested, returned result: {}", id, item);

        return item.orElseThrow(() -> new EntityNotFoundException("item with id: " + id + " doesn't exists"));

    }

    public List<ItemDto> getAll(long userId) {
        List<ItemDto> itemList = itemDao.getAll(userId).stream()
                .filter(Objects::nonNull)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        log.debug("all items requested: {}", itemList.size());

        return itemList;

    }

    @Override
    public Long save(Item item, long userId) {
        item.setOwner(userDao.get(userId)
                .orElseThrow(() -> new EntityNotFoundException("user id: " + userId + " not found")));
        itemDao.save(item);
        log.debug("new item created: {}", item);
        return item.getId();
    }

    @Override
    public List<Item> search(String text, long userId) {
        log.debug("search in items with query: {} requested", text);

        return itemDao.search(text, userId).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        Item itemToUpdate = itemDao.get(itemDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("item id: " + itemDto.getId() + " not found"));

        Map<String, Object> classProperties = ClassProperties.getClassProperties(itemDto, false);

        classProperties.forEach((k, v) -> { //TODO verify !!!
            Method method = ReflectionUtils.findMethod(Item.class, "set" + StringUtils.capitalize(k));
            if (method != null) {
                ReflectionUtils.invokeMethod(method, itemToUpdate, classProperties.get(k));
            }
        });
        return ItemMapper.toItemDto(itemToUpdate);
    }


}
