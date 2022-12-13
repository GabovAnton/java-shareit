package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.item.Comment} entity
 */
@Getter
@Setter
@RequiredArgsConstructor
public class CommentDto implements Serializable {
    private final Long id;
    private final String text;
    private final Long authorId;
    private final String authorName;
    private final LocalDateTime created;
}