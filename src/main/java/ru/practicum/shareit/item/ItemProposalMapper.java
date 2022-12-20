package ru.practicum.shareit.item;

import org.mapstruct.*;
import ru.practicum.shareit.request.ItemProposal;
import ru.practicum.shareit.request.ItemProposalDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ItemProposalMapper {
    ItemProposal toEntity(ItemProposalDto itemProposalDto);

    ItemProposalDto toDto(ItemProposal itemProposal);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ItemProposal partialUpdate(ItemProposalDto itemProposalDto, @MappingTarget ItemProposal itemProposal);
}