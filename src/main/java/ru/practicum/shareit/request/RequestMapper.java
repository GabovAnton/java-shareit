package ru.practicum.shareit.request;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RequestMapper {
    Request requestDtoToRequest(RequestDto requestDto);

    RequestWithProposalsDto requestToRequestWithProposalDto(Request request);

    Request requestDtoToRequest(RequestWithProposalsDto requestDto);

    RequestDto requestToRequestDto(Request request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Request updateRequestFromRequestDto(RequestDto requestDto, @MappingTarget Request request);
}
