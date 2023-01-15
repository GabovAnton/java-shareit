package ru.practicum.shareit.gateway.request;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.gateway.request.dto.RequestDto;
import ru.practicum.shareit.gateway.request.dto.RequestWithProposalsDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"requests"})
public class RequestService {

    private final RequestFeignClient requestFeignClient;

    public List<RequestWithProposalsDto> getAll(
            long userId, Integer from, Integer size) {

        return requestFeignClient.getAll(userId, from, size);
    }

    public List<RequestWithProposalsDto> getAllFromOthers(
            long userId, Integer from, Integer size) {

        return requestFeignClient.getAllFromOthers(userId, from, size);
    }

    public RequestDto create(
            long userId, RequestDto requestDto) {

        return requestFeignClient.create(userId, requestDto);
    }

    public RequestWithProposalsDto getRequestById(
            long requestId, long userId) {

        return requestFeignClient.getRequestById(requestId, userId);
    }

}
