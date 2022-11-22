package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Builder
@Data
@AllArgsConstructor
public class UserDto {
    public interface New {
    }

    public interface Update {
    }

    public interface SimpleView {
    }

    public interface AdminView extends ItemDto.SimpleView {
    }

    @Null(groups = {New.class})
    @JsonView({SimpleView.class})
    private long id;

    @NotNull(groups = {New.class, Update.class})
    @JsonView({SimpleView.class})
    private String name;

    @NotBlank(groups = {New.class, Update.class})
    @Email(groups = {New.class, Update.class})
    @JsonView({SimpleView.class})
    private String email;
}
