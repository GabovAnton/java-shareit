package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

@Sql(scripts = {"classpath:/SampleData.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShareItTests {

    private final UserService userService;
    private final ItemService itemService;
    private final UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testDatabaseIsNotEmpty() {
        List<UserDto> all = userService.getAll();
        assertThat(all)
                .isNotEmpty();

    }

    @Test
    public void CreateUserShouldNotThrowExceptionOnSave() {
        UserDto userOne = new UserDto(null, "testName", "test@gmail.com", null);

        assertThat(userService.save(userMapper.userDtoToUser(userOne)))
                .extracting(User::getName)
                .isEqualTo("testName");
    }

    @Test
    public void CreateUserWithDuplicateEmailShouldThrowExceptionOnSave() {

        UserDto userOne = new UserDto(null, "testName", "agabov@gmail.com", null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.save(userMapper.userDtoToUser(userOne));
        });

    }

    @Test
    public void UpdateUserWithDuplicateEmailShouldThrowExceptionOnSave() {
        User user = userService.getUser(1);
        user.setEmail("ivan@gmail.com");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.save(user);
        });
    }

    @Test
    public void UpdateUserShouldNotThrowExceptionOnSave() {
        User user = userService.getUser(1);
        user.setEmail("anton2@gmail.com");

        assertThat(userService.save(user))
                .extracting(User::getEmail)
                .isEqualTo("anton2@gmail.com");
    }
    @Test
    public void DeleteUserShouldNotThrowExceptionOnSave() {

        assertThat(userService.delete(1L));

    }
    @Test
    public void CreateItemShouldNotThrowExceptionOnSave() {
        Item item = new Item();
        item.setName("Test item");
        item.setAvailable(true);
        item.setDescription("new interesting item");
        item.setOwner(userService.getUser(1L));

        assertThat(itemService.save(item, 1L)
                .getName()
                .equals("Test item"));
    }


    @Test
    public void SearchInDescriptionShouldReturnOneRecord() {
        List<ItemDto> searchResult = itemService.search("оТверТ");

        assertThat(searchResult)
                .isNotEmpty()
                .hasSize(1)
                .extracting(ItemDto::getDescription)
                .contains("Аккумуляторная отвертка");
    }

    @Test
    public void GetAllItemsForUserOneShouldReturnOneRecord() {
        List<ItemDto> searchResult = itemService.getAll(1);

        assertThat(searchResult)
                .isNotEmpty()
                .hasSize(1)
                .extracting(ItemDto::getName)
                .contains("Аккумуляторная отвертка");
    }

}
