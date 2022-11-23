package ru.practicum.shareit.item.service;

import exception.EntityNotFoundException;
import exception.ShareItValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.utils.ClassProperties;

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
    public List<ItemDto> search(String text, long userId) {
        log.debug("search in items with query: {} requested", text);
        return StringUtils.isNotEmpty(text) ?
                itemDao.search(text.toLowerCase(), userId).stream()
                        .filter(Objects::nonNull)
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)) :
                new ArrayList<>();
    }

    @Override
    public ItemDto update(ItemPatchDto itemPatchDto, long userId) {
        Item itemToUpdate = itemDao.get(itemPatchDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("item id: " + itemPatchDto.getId() + " not found"));
        if (itemToUpdate.getOwner().getId() != userId) {
            throw new ShareItValidationException("error while trying to update item which belongs to another user");
        }

        Map<String, Object> classProperties = ClassProperties.getClassProperties(itemPatchDto);

        classProperties.forEach((k, v) -> {
            Class<Item> clz = Item.class;
            Arrays.stream(clz.getDeclaredMethods()).
                    filter(x -> x.getName().equals("set" + StringUtils.capitalize(k))).
                    findAny().ifPresent(y -> ReflectionUtils.invokeMethod((y), itemToUpdate, classProperties.get(k)));

        });
        return ItemMapper.toItemDto(itemToUpdate);
    }


}
