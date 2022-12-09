package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.*;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase//(replace= AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)

@Sql(scripts = {"classpath:/SampleData.sql"},
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShareItTests {

private final UserService userService;
private final ItemService itemService;

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
	public void CreateUserWithDuplicateEmailShouldThrowExceptionOnSave() {


		UserDto userOne = new UserDto(null,"testName", "agabov@gmail.com", null);


		assertThrows(DataIntegrityViolationException.class, () -> {
			userService.save(UserMapper.INSTANCE.userDtoToUser(userOne));
			//userRepository.flush();
		});


	}

	//оТверТ
	@Test
	public void SearchInDescriptionShouldReturnOneRecord() {
		List<ItemDto> searchResult = itemService.search("оТверТ");

		assertThat(searchResult)
				.isNotEmpty()
				.hasSize(1)
				.extracting(ItemDto::getDescription)
				.contains("Аккумуляторная отвертка");
	}

}
