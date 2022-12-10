package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.ItemDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
public class UserDto {

    @JsonView({SimpleView.class})
    private Long id;

    @NotNull(groups = {New.class})
    @JsonView({SimpleView.class})
    private String name;
    @NotBlank(groups = {New.class})
    @Email(groups = {New.class})
    @JsonView({SimpleView.class})
    private String email;

    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd, hh:mm:ss");
    //@NotBlank(groups = {New.class})
    @Email(groups = {New.class})
    @JsonView({SimpleView.class})
    private String registrationDate;

    public interface New {
    }

    public interface Update {
    }

    public interface SimpleView {
    }




}
