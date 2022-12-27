package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentDto implements Serializable {

    private  Long id;

    private  String text;

    private  Long authorId;

    private  String authorName;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern="uuuu-MM-dd'T'HH:mm:ss")
    private  LocalDateTime created;
}