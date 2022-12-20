package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {
    List<RequestWithProposalsDto> getAll(Long userId);
    RequestDto SaveRequest(RequestDto requestDto, Long userId);

    List<RequestDto> getAllFromOthers(int from, int size, Long userId);

    RequestWithProposalsDto GetRequest(Long requestId, Long userId);


}
