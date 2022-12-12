package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.item.Comment} entity
 */
@Data
@RequiredArgsConstructor
public class CommentDto implements Serializable {
    @JsonView({ItemDto.SimpleView.class})
    private final Long id;
    @JsonView({ItemDto.SimpleView.class})
    private final String text;
    @JsonView({ItemDto.SimpleView.class})
    private final Long authorId;
    @JsonView({ItemDto.SimpleView.class})
    private final String authorName;
    @JsonView({ItemDto.SimpleView.class})
    private final LocalDateTime created;
}