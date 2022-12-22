package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {
    List<RequestWithProposalsDto> getAll(Long userId);
    RequestDto SaveRequest(RequestDto requestDto, Long userId);

    List<RequestWithProposalsDto> getAllFromOthers(Integer from, Integer size, Long userId);

    RequestWithProposalsDto GetRequest(Long requestId, Long userId);


}
