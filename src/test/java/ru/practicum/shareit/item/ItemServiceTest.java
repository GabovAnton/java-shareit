package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.ShareItValidationException;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private final EntityManager em;

    LocalDateTime currentDate = LocalDateTime
            .of(2022, 12, 10, 5, 5, 5, 5);


    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @InjectMocks
    private ItemService itemService = new ItemServiceImpl(
            itemRepository,
            userService,
            commentRepository,
            bookingRepository
    );

    @Test
    void getItemDto_WrongItemShouldThrowException() {
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);

        Long ItemId = 100L;
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            itemService.getItemDto(100L, 100L);
        });
        assertThat(entityNotFoundException.getMessage(),
                equalTo("item with id: " + ItemId + " doesn't exists"));
    }

    @Test
    void getItemDto() {
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);

        Long itemId = 100L;
        when(itemRepository.findById(anyLong()))
                .thenThrow(new EntityNotFoundException("item with id: " + itemId + " doesn't exists"));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            itemService.getItemDto(100L, 100L);
        });
        assertThat(entityNotFoundException.getMessage(),
                equalTo("item with id: " + itemId + " doesn't exists"));
    }

    @Test
    void map_WrongItemShouldThrowException() {
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);

        Long itemId = 100L;
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            itemService.map(100L);
        });
        assertThat(entityNotFoundException.getMessage(),
                equalTo("item with id: " + itemId + " doesn't exists"));
    }


    @Test
    void save_ShouldNotThrowException() {
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemService, "userService", userService);
        Item item = makeItem();
        when(itemRepository.save(any()))
                .thenReturn(item);
        itemService.save(item, 100L);

        Mockito.verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertThat(savedItem.getDescription(), equalTo(item.getDescription()));
        assertThat(savedItem.getId(), equalTo(item.getId()));

    }

    @Test
    void saveComment_ShouldThrowExceptionOnBlankText() {
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemService, "userService", userService);
        Comment comment = makeComment();
        CommentDto commentDto = CommentMapper.INSTANCE.commentToCommentDto(comment);

        assertThat(comment.getText(), equalTo(commentDto.getText()));
        commentDto.setText("");

        ForbiddenException ForbiddenException = assertThrows(ForbiddenException.class, () -> {
            itemService.saveComment(100L, 100L, commentDto);
        });

        assertThat(ForbiddenException.getMessage(),
                equalTo("Comment should not be empty"));
        verify(commentRepository, never()).save(comment);

    }

    @Test
    void saveComment_ShouldThrowExceptionOnUnfinishedBooking() {
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemService, "userService", userService);
        ReflectionTestUtils.setField(itemService, "bookingRepository", bookingRepository);
        Comment comment = makeComment();
        CommentDto commentDto = CommentMapper.INSTANCE.commentToCommentDto(comment);

        assertThat(comment.getText(), equalTo(commentDto.getText()));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeItem()));
        when(userService.getUser(anyLong()))
                .thenReturn(makeUser());
        when(bookingRepository.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(anyLong(), anyLong(),
                any(), any()))
                .thenReturn(false);

        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> {
            itemService.saveComment(100L, 100L, commentDto);
        });

        assertThat(forbiddenException.getMessage(),
                equalTo("error while trying to add comment to item which hasn't  finished booking by user"));
        verify(commentRepository, never()).save(comment);

    }

    @Test
    void update_ShouldThrowExceptionOnUpdateItemWhichIsNotBelongToUser() {
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemService, "userService", userService);

        ItemPatchDto itemPatchDto = makeItemPatchDto();

        Item item = makeItem();
        User user = makeUser();
        item.setOwner(user);

        item.setOwner(user);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        ShareItValidationException shareItValidationException = assertThrows(ShareItValidationException.class, () -> {
            itemService.update(itemPatchDto, 200L);
        });

        assertThat(shareItValidationException.getMessage(),
                equalTo("error while trying to update item which belongs to another user"));

        verify(itemRepository, never()).save(any());

    }

    @Test
    void isItemAvailable_ShouldThrowExceptionOnUnavailableItem() {
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);

        Long itemId = 100L;

        ItemShortAvailability itemShortAvailability = new ItemShortAvailability() {
            @Override
            public Boolean getAvailable() {
                return false;
            }
        };

        when(itemRepository.isItemAvailable(anyLong()))
                .thenReturn(Optional.of(itemShortAvailability));


        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> {
            itemService.isItemAvailable(itemId);
        });

        assertThat(forbiddenException.getMessage(),
                equalTo("item with id: " + itemId + " is unavailable for booking"));
        verify(itemRepository, never()).save(any());

    }

    private ItemDto makeItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(100L);
        itemDto.setName("thing");
        itemDto.setDescription("just simple thing");
        itemDto.setAvailable(true);
        itemDto.setRequestId(100L);
        itemDto.setLastBooking(new ItemLastBookingDto(100L, 100L));
        itemDto.setNextBooking(new ItemNextBookingDto(100L, 100L));

        CommentDto commentDto = new CommentDto(100L,
                "good thing",
                101L,
                "Artur",
                LocalDateTime.now().minusDays(100));

        itemDto.setComments(Set.of(commentDto));
        itemDto.setOwner(new UserDto());

        return itemDto;
    }


    private User makeUser() {
        User user = new User();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");

        return user;
    }

    private Comment makeComment() {
        Comment comment = new Comment();
        comment.setAuthor(makeUser());
        comment.setCreated(currentDate.minusDays(15));
        comment.setText("good thing");

        return comment;

    }

    private Item makeItem() {
        Item item = new Item();
        item.setId(100L);
        Comment comment = makeComment();
        comment.setItem(item);
        item.setItemComments(Set.of(comment));
        item.setName("simple thing");
        item.setDescription("just simple thig, nothing interesting");
        item.setAvailable(true);
        return item;
    }


    private ItemPatchDto makeItemPatchDto() {
        return new ItemPatchDto(
                100L,
                "simple thing",
                "just simple thig, nothing interesting",
                true,
                100L
        );
    }

}