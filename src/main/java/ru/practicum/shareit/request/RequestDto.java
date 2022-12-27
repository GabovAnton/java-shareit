package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.UserDto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.request.Request} entity
 */
@Data
@Builder
public class RequestDto implements Serializable {

    private final Long id;

    @NotBlank
    private final String description;

    private final UserDto requester;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern="uuuu-MM-dd'T'HH:mm:ss")
    private final LocalDateTime created;

}