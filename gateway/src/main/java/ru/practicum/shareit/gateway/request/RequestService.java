package ru.practicum.shareit.gateway.request;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.gateway.request.dto.RequestDto;
import ru.practicum.shareit.gateway.request.dto.RequestWithProposalsDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@CacheConfig(cacheNames = {"requests"})
public class RequestService {

    private final RequestFeignClient requestFeignClient;

    public List<RequestWithProposalsDto> getAll(long userId, @PositiveOrZero Integer from, @Positive Integer size) {

        return requestFeignClient.getAll(userId, from, size);
    }

    public List<RequestWithProposalsDto> getAllFromOthers(long userId,
            @PositiveOrZero Integer from,
            @Positive Integer size) {

        return requestFeignClient.getAllFromOthers(userId, from, size);
    }

    public RequestDto create(long userId, @Valid RequestDto requestDto) {

        return requestFeignClient.create(userId, requestDto);
    }

    public RequestWithProposalsDto getRequestById(long requestId, long userId) {

        return requestFeignClient.getRequestById(requestId, userId);
    }

}
