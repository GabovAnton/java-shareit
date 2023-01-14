package ru.practicum.shareit.gateway.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RequestDto implements Serializable {

    private final Long id;

    @NotBlank
    private final String description;

    private final UserDto requester;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss")
    private final LocalDateTime created;

}