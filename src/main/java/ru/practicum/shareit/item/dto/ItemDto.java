package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
public class ItemDto {


    public interface SimpleView {
    }

    public interface AdminView extends SimpleView {
    }

    @JsonView({SimpleView.class})
    private long id;

    @NotBlank
    @JsonView({SimpleView.class})
    private String name;

    @NotNull
    @JsonView({SimpleView.class})
    private String description;

    @JsonView({SimpleView.class})
    @NotNull
    private Boolean available;

    @JsonView({AdminView.class})
    private User owner;

    @JsonView({AdminView.class})
    private Long requestId;

}

