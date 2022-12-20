package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemProposalsRepository extends JpaRepository<ItemProposal, Long> {
}