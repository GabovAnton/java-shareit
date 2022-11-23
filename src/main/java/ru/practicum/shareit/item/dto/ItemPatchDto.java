package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;


@Data
@AllArgsConstructor
public class ItemPatchDto {
    public interface SimpleView {
    }

    public interface AdminView extends ItemDto.SimpleView {
    }

    @JsonView({ItemDto.SimpleView.class})
    private long id;

    @JsonView({ItemDto.SimpleView.class})
    private String name;

    @JsonView({ItemDto.SimpleView.class})
    private String description;

    @JsonView({ItemDto.SimpleView.class})
    private Boolean available;

    @JsonView({ItemDto.AdminView.class})
    private User owner;

    @JsonView({ItemDto.AdminView.class})
    private Long requestId;
}
