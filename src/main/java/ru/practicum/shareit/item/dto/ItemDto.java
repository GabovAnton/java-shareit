package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class ItemDto {


    public interface SimpleView {
    }

    public interface AdminView extends SimpleView {
    }

    @JsonView({SimpleView.class})
    //@Null
    private long id;

    //@   NotNull
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

