package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * TODO Sprint add-controllers.
 */

@Builder
@Data
@AllArgsConstructor
public class ItemDto {

    public interface New {
    }

    public interface Update {
    }

    public interface SimpleView {
    }

    public interface AdminView extends SimpleView {
    }

    //@Null(groups = {New.class})
    @JsonView({SimpleView.class})
    private long id;

    @NotNull(groups = {New.class, Update.class})
    @JsonView({SimpleView.class})
    private String name;

    @NotNull(groups = {New.class, Update.class})
    @JsonView({SimpleView.class})
    private String description;

    @NotNull(groups = {New.class, Update.class})
    @JsonView({SimpleView.class})
    private boolean isAvailable;


    @JsonView({AdminView.class})
    private User owner;

    @JsonView({AdminView.class})
    private Long requestId;

}

