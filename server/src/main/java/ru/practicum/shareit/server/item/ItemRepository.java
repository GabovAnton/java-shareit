package ru.practicum.shareit.server.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long id);

    @Query("select i from Item i where i.id = ?1")
    Optional<ItemShortAvailability> isItemAvailable(Long id);

}