package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {

    List<RequestWithProposalsDto> getAll(Long userId);

    RequestDto saveRequest(RequestDto requestDto, Long userId);

    List<RequestWithProposalsDto> getAllFromOthers(Integer from, Integer size, Long userId);

    RequestWithProposalsDto getRequest(Long requestId, Long userId);

}
