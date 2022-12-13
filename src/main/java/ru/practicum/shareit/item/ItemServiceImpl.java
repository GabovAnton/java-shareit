package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.ShareItValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemDto getItemDto(long id, long userId) {


        Item item = itemRepository
                .findById(id).orElseThrow(() -> new EntityNotFoundException("item with id: " + id + " doesn't exists"));
        log.debug("item with id: {} requested, returned result: {}", id, item);
        ItemDto itemDto = itemMapper.itemToItemDto(item);

        itemDto.setComments(commentMapper.map(item.getItemComments()));
        Comparator<Booking> byDateEnd = Comparator
                .comparing(Booking::getEnd);
        List<Booking> bookings = item.getItemBookings().stream()
                .filter(x -> x.getItem().getOwner().getId().equals(userId)).sorted(byDateEnd).limit(2)
                .collect(Collectors.toList());

        return getItemDtoWithLastAndNextBookings(itemDto, bookings);

    }


    public Item map(long id) {
        Optional<Item> item = itemRepository.findById(id);
        log.debug("item with id: {} requested, returned result: {}", id, item);
        return item.orElseThrow(() -> new EntityNotFoundException("item with id: " + id + " doesn't exists"));
    }

    public List<ItemDto> getAll(long userId) {

        List<ItemDto> itemList = itemRepository.findByOwner_Id(userId).stream()
                .filter(Objects::nonNull)
                .map(x -> {

                    Comparator<Booking> byDateEnd = Comparator
                            .comparing(Booking::getEnd);

                    ItemDto itemDto = itemMapper.itemToItemDto(x);
                    itemDto.setComments(commentMapper.map(x.getItemComments()));
                    List<Booking> bookings = x.getItemBookings().stream()
                            .filter(y -> y.getItem().getOwner().getId().equals(userId)).sorted(byDateEnd).limit(2)
                            .collect(Collectors.toList());
                    return getItemDtoWithLastAndNextBookings(itemDto, bookings);
                })
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));


        log.debug("all items requested: {}", itemList.size());

        return itemList;
    }

    private ItemDto getItemDtoWithLastAndNextBookings(ItemDto itemDto, List<Booking> bookings) {
        if (bookings.size() > 1) {
            Long lastBookingId = getBookingId(bookings, 0);
            Long lastBookerId = getBookerId(bookings, 0);
            itemDto.setLastBooking(lastBookingId != null && lastBookerId != null ? new ItemLastBookingDto(lastBookingId,
                    lastBookerId) : null);

            Long nextBookingId = getBookingId(bookings, 1);
            Long nextBookerId = getBookerId(bookings, 1);
            itemDto.setNextBooking(lastBookingId != null && lastBookerId != null ? new ItemNextBookingDto(nextBookingId,
                    nextBookerId) : null);
        }

        return itemDto;
    }

    private Long getBookingId(List<Booking> bookings, int rowNumber) {
        return (bookings.size() > 0 && bookings.get(rowNumber) != null && bookings.get(rowNumber).getId() != null)
                ? bookings.get(rowNumber).getId() : null;
    }

    private Long getBookerId(List<Booking> bookings, int rowNumber) {
        return (bookings.size() > 0 && bookings.get(rowNumber) != null && bookings.get(rowNumber).getBooker() != null
                && bookings.get(rowNumber).getBooker().getId() != null)
                ? bookings.get(rowNumber).getBooker().getId() : null;
    }

    @Override
    public Item save(Item item, long userId) {
        item.setOwner(userService.getUser(userId));
        Item savedItem = itemRepository.save(item);
        log.debug("new item created: {}", item);
        return savedItem;
    }

    @Override
    public Comment saveComment(Long itemId, Long userId, CommentDto commentDto) {
        if (StringUtils.isBlank(commentDto.getText())) {
            throw new ForbiddenException("Comment should not be empty");
        }
        User author = userService.getUser(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("item with id: " + itemId + " doesn't exists"));

        if (bookingRepository.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(itemId, userId,
                BookingStatus.APPROVED, LocalDateTime.now())) {
            Comment comment = commentMapper.commentDtoToComment(commentDto);
            comment.setAuthor(author);
            comment.setItem(item);
            comment.setCreated(LocalDateTime.now());
            commentRepository.save(comment);
            return comment;
        } else {
            throw new ForbiddenException
                    ("error while trying to add comment to item which hasn't  finished booking by user");
        }
    }

    @Override
    public List<ItemDto> search(String text) {

        String query = "%" + StringUtils.toRootLowerCase(text) + "%";
        log.debug("search in items with query: {} requested", query);
        return StringUtils.isNotEmpty(text) ?
                itemRepository.findByNameOrDescription(query).stream()
                        .filter(Objects::nonNull)
                        .map(itemMapper::itemToItemDto)
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

        Item item = itemMapper.updateItemFromItemDto(itemPatchDto, itemToUpdate);
        Item save = itemRepository.save(item);
        return itemMapper.itemToItemDto(save);
    }

    @Override
    public Boolean isItemAvailable(long itemId) {

        if (Boolean.TRUE.equals(itemRepository.isItemAvailable(itemId)
                .orElseThrow(() -> new EntityNotFoundException("item id: " + itemId + " not found"))
                .getAvailable())) {
            return true;
        } else {
            throw new ForbiddenException("item with id: " + itemId + " is unavailable for booking");
        }

    }


}
