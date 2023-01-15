package ru.practicum.shareit.gateway.item;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.gateway.item.dto.CommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.item.dto.ItemPatchDto;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8089)
@SpringBootTest(properties = "SHAREIT_SERVER_URL=http://localhost:8089")

@ExtendWith(SpringExtension.class)
class ItemServiceTest {

    LocalDateTime currentDate = LocalDateTime.of(2022, 12, 10, 5, 5, 5, 5);

    @Autowired
    private ItemFeignClient itemFeignClient;

    @Test
    void getItemShouldReturnDto() {

        stubFor(get("/items/100")
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("oneItem-response.json")));

        ItemDto item = itemFeignClient.getItem(100L, 1L);
        assertThat(item).isNotNull().extracting(ItemDto::getId).isEqualTo(100L);

    }

    @Test
    void getAllShouldReturnItemsList() {

        stubFor(get("/items?from=0&size=10")
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("items-response.json")));
        List<ItemDto> itemDtos = itemFeignClient.getAll(1L, 0, 10);

        assertThat(itemDtos).isNotEmpty().hasSize(1).extracting(ItemDto::getId).contains(100L);
    }

    @Test
    void createShouldReturnNewDto() {

        stubFor(post("/items")
                .withRequestBody(equalToJson(
                        "{  \"id\" : 100, \"name\" : \"updated thing\", \"description\" : \"just simple thig, nothing interesting\", \"available\" : true, \"requestId\" : null, \"lastBooking\" : null, \"nextBooking\" : null, \"comments\" : null, \"owner\" : null}",
                        true,
                        true))
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("oneItem-response.json")));
        ItemDto itemDto = itemFeignClient.create(1L, makeItemDto());

        assertThat(itemDto).isNotNull().extracting(ItemDto::getId).isEqualTo(100L);
    }

    @Test
    void updateShouldReturnUpdatedDto() {

        stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .patch(urlEqualTo("/items/100"))
                .withRequestBody(equalToJson(
                        "{  \"id\" : 100, \"name\" : \"simple thing\", \"description\" : \"just simple thig, nothing interesting\", \"available\" : true, \"requestId\" : 100}",
                        true,
                        true))
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("updateItem-response.json")));
        ItemDto itemDto = itemFeignClient.update(1L, 100L, makeItemPatchDto());

        assertThat(itemDto).isNotNull().extracting(ItemDto::getName).isEqualTo("updated thing");
    }

    @Test
    void search() {

        stubFor(get("/items/search?text=test&from=0&size=10")
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("items-response.json")));
        List<ItemDto> itemDtos = itemFeignClient.search(1L, 0, 10, "test");
        assertThat(itemDtos).isNotEmpty().hasSize(1).extracting(ItemDto::getId).contains(100L);

    }

    @Test
    void postComment() {

        stubFor(post("/items/1/comment")
                .withRequestBody(equalToJson(
                        "{ \"id\" : null, \"text\" : \"good thing\", \"authorId\" : 100, \"authorName\" : \"Artur\", \"created\" : \"2022-11-25T05:05:05\"}\n",
                        true,
                        true))
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("oneItem-response.json")));
        itemFeignClient.postComment(1L, makeCommentDto(), 1L);
    }

    private ItemPatchDto makeItemPatchDto() {

        return new ItemPatchDto(100L, "simple thing", "just simple thig, nothing interesting", true, 100L);
    }

    private ItemDto makeItemDto() {

        ItemDto itemDto = new ItemDto();
        itemDto.setId(100L);
        itemDto.setName("updated thing");
        itemDto.setDescription("just simple thig, nothing interesting");
        itemDto.setAvailable(true);

        return itemDto;
    }

    private CommentDto makeCommentDto() {

        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorName("Artur");
        commentDto.setAuthorId(100L);
        commentDto.setCreated(currentDate.minusDays(15));
        commentDto.setText("good thing");

        return commentDto;

    }

}