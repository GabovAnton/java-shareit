package ru.practicum.shareit.request;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RequestMapper {
    Request requestDtoToRequest(RequestDto requestDto);

    RequestWithProposalsDto requestToRequestWithProposalDto(Request request);

    RequestDto requestToRequestDto(Request request);
}
