package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
public class UserUpdateDto implements Serializable {
    private final Long id;
    private final Set<ItemDto> items;
    private final Set<CommentDto> Comments;
    private final String name;
    private final String email;
    private final LocalDate registrationDate;


    @Data
    public static class ItemDto implements Serializable {
        private final Long id;
        private final String name;
        private final String description;
        private final Boolean available;
        private final Integer requestId;
    }


    @Data
    public static class CommentDto implements Serializable {
        private final Long id;
        private final String text;
        private final LocalDateTime created;
    }
}