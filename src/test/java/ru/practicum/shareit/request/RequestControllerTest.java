package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RequestService requestService;

    @InjectMocks
    RequestController requestController;

    @Autowired
    private MockMvc mvc;

    LocalDateTime currentDate = LocalDateTime
            .of(2022, 12, 10, 5, 5, 5, 5);

    @Test
    void getAll_ShouldReturnList() throws Exception {
        RequestWithProposalsDto requestWithProposalsDto = makeRequestWithProposalsDto();

        List<RequestWithProposalsDto> expectedItems = List.of(requestWithProposalsDto);

        when(requestService.getAll(anyLong()))
                .thenReturn(expectedItems);


        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header("X-Sharer-User-Id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", equalTo(requestWithProposalsDto.getDescription())))
                .andExpect(jsonPath("$[0].id", is(requestWithProposalsDto.getId()), Long.class));

    }

    @Test
    void create_ShouldReturnSameObject() throws Exception {
        RequestDto requestDto = makeRequestDto();


        when(requestService.saveRequest(any(), anyLong()))
                .thenReturn(requestDto);


        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andDo(print())
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", equalTo(requestDto.getDescription())));

    }

    @Test
    void getAllFromOthers_ShouldReturnList() throws Exception {

        RequestWithProposalsDto requestWithProposalsDto = makeRequestWithProposalsDto();

        List<RequestWithProposalsDto> expectedItems = List.of(requestWithProposalsDto);

        when(requestService.getAllFromOthers(anyInt(),anyInt(),anyLong()))
                .thenReturn(expectedItems);


        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .param("from", "5")
                        .param("size", "5")
                        .header("X-Sharer-User-Id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", equalTo(requestWithProposalsDto.getDescription())))
                .andExpect(jsonPath("$[0].id", is(requestWithProposalsDto.getId()), Long.class));
    }

    @Test
    void getBookingById() throws Exception {

        RequestWithProposalsDto requestWithProposalsDto = makeRequestWithProposalsDto();


        when(requestService.GetRequest(anyLong(),anyLong()))
                .thenReturn(requestWithProposalsDto);


        mvc.perform(get("/requests/{requestId}", 100L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .header("X-Sharer-User-Id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", equalTo(requestWithProposalsDto.getDescription())))
                .andExpect(jsonPath("$.id", is(requestWithProposalsDto.getId()), Long.class));

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

    private UserDto makeUserDto() {
        UserDto user = new UserDto();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");
        user.setRegistrationDate(currentDate.minusDays(15));
        return user;
    }

    private RequestDto makeRequestDto() {
        return new RequestDto(
                100L,
                "simple test description",
                makeUserDto(),
                currentDate.minusDays(1)

        );

    }

    private RequestWithProposalsDto makeRequestWithProposalsDto (){
        return new RequestWithProposalsDto(
                100L,
                "some item",
                makeUserDto(),
                currentDate.minusDays(2),
                List.of(makeItemDto())
        );
    }
}