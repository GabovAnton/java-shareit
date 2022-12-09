package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.ShareItValidationException;
import ru.practicum.shareit.user.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;


    public Item getItem(long id) {
        Optional<Item> item = itemRepository.findById(id);
        log.debug("item with id: {} requested, returned result: {}", id, item);

        return item.orElseThrow(() -> new EntityNotFoundException("item with id: " + id + " doesn't exists"));

    }


    public List<ItemDto> getAll(long userId) {
        List<ItemDto> itemList = itemRepository.findByOwner_Id(userId).stream()
                .filter(Objects::nonNull)
                .map(ItemMapper.INSTANCE::itemToItemDto)
                .map(x -> {
                    List<Booking> bookings = bookingRepository
                            .findByItem_Owner_IdAndItem_IdOrderByStartDesc(userId, x.getId(),
                                    PageRequest.of(0, 2));
                    if (bookings.size() > 0) {
                        x.setNextBooking((bookings.get(0) != null && bookings.get(0).getStart() != null)
                                ? bookings.get(0).getStart() : null);
                        x.setLastBooking((bookings.get(1) != null && bookings.get(1).getStart() != null)
                                ? bookings.get(1).getStart() : null);

                    }
                    return x;
                })
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

        log.debug("all items requested: {}", itemList.size());

        return itemList;
    }

    @Override
    public Item save(Item item, long userId) {
        item.setOwner(userService.getUser(userId));
        Item savedItem = itemRepository.save(item);
        log.debug("new item created: {}", item);
        return savedItem;
    }

    @Override
    public List<ItemDto> search(String text) {

        String query = "%" + StringUtils.toRootLowerCase(text) + "%";
        log.debug("search in items with query: {} requested", query);
        return StringUtils.isNotEmpty(text) ?
                itemRepository.findByNameOrDescription(query).stream()
                        .filter(Objects::nonNull)
                        .map(ItemMapper.INSTANCE::itemToItemDto)
                        .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)) :
                new ArrayList<>();


    }

    @Override
    public ItemDto update(ItemPatchDto itemPatchDto, long userId) {

        Item itemToUpdate = itemRepository.findById(itemPatchDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("item id: " + itemPatchDto.getId() + " not found"));

        if (itemToUpdate.getOwner() != null && itemToUpdate.getOwner().getId() != userId) {
            throw new ShareItValidationException("error while trying to update item which belongs to another user");
        }

        ItemMapper.INSTANCE.updateItemFromItemDto(itemPatchDto, itemToUpdate);
        itemRepository.save(itemToUpdate);
        return ItemMapper.INSTANCE.itemToItemDto(itemToUpdate);
    }

    @Override
    public Boolean isItemAvailable(long itemId) {

        if (itemRepository.isItemAvailable(itemId)
                .orElseThrow(() -> new EntityNotFoundException("item id: " + itemId + " not found"))
                .getAvailable()) {
            return true;
        } else {
            throw new ForbiddenException("item with id: " + itemId + " is unavailable for booking");
        }

    }


}
