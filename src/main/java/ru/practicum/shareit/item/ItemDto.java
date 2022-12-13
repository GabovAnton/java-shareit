package ru.practicum.shareit.item;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDto {


    private long id;

    @NotBlank
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Boolean available;

    @Data
    public static class owner implements Serializable {
        private final Long id;
        private final String name;
        private final String email;
        private final LocalDate registrationDate;
    }

    private Long requestId;

    private ItemLastBookingDto lastBooking;

    private ItemNextBookingDto nextBooking;

    private Set<CommentDto> comments;
}

