package ru.practicum.shareit.gateway.request;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.gateway.request.dto.RequestDto;
import ru.practicum.shareit.gateway.request.dto.RequestWithProposalsDto;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8089)
@SpringBootTest(properties = "SHAREIT_SERVER_URL=http://localhost:8089")

@ExtendWith(SpringExtension.class)
class RequestServiceTest {

    LocalDateTime currentDate = LocalDateTime.of(2022, 12, 10, 5, 5, 5, 5);

    @Autowired
    private RequestFeignClient requestFeignClient;

    @Test
    void getAllShouldRequestItemsList() {
        stubFor(get("/requests?from=0&size=10")
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("requests-response.json")));
        List<RequestWithProposalsDto> requestWithProposalsDtos = requestFeignClient.getAll(1L, 0, 10);
        assertThat(requestWithProposalsDtos).isNotEmpty().hasSize(1).element(0).
                extracting(RequestWithProposalsDto::getId).isEqualTo(100L);

    }

    @Test
    void getAllFromOthers() {
        stubFor(get("/requests/all?from=0&size=10")
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("requests-response.json")));
        List<RequestWithProposalsDto> requestWithProposalsDtos = requestFeignClient.getAllFromOthers(1L, 0, 10);
        assertThat(requestWithProposalsDtos).isNotEmpty().hasSize(1).element(0).
                extracting(RequestWithProposalsDto::getId).isEqualTo(100L);


    }

    @Test
    void createShouldReturnNewDto() {

        stubFor(post("/requests")
                .withRequestBody(equalToJson("{\"id\":100,\"description\":\"simple test description\",\"requester\":{\"id\":100,\"name\":\"Artur\",\"email\":\"artur@gmail.com\",\"registrationDate\":\"2022-11-25T05:05:05\"},\"created\":\"2022-12-09T05:05:05\"}",
                        true,
                        true))
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("oneRequest-response.json")));

        RequestDto requestDto = requestFeignClient.create(1L, makeRequestDto());

        assertThat(requestDto).isNotNull().extracting(RequestDto::getId).isEqualTo(100L);

    }

    @Test
    void getRequestById() {
        stubFor(get("/requests/100")
                .withHeader("X-Sharer-User-Id", WireMock.matching("1"))
                .willReturn(WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("oneRequest-response.json")));
        RequestWithProposalsDto requestDto = requestFeignClient.getRequestById(100L, 1L);
        assertThat(requestDto).isNotNull().extracting(RequestWithProposalsDto::getId).isEqualTo(100L);
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

        return new RequestDto(100L, "simple test description", makeUserDto(), currentDate.minusDays(1)

        );

    }

   /* private RequestWithProposalsDto makeRequestWithProposalDto() {
RequestWithProposalsDto request = new RequestWithProposalsDto();

        return new RequestWithProposalsDto(100L, "simple test description", makeUserDto(), currentDate.minusDays(1)

        );

    }*/

}