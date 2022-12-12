package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


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
    //@JsonIgnore
    private User owner;

    @JsonView({AdminView.class})
    private Long requestId;

    @JsonView({SimpleView.class})
    private ItemLastBookingDto lastBooking;

    @JsonView({SimpleView.class})
    private ItemNextBookingDto nextBooking;

    @JsonView({SimpleView.class})
    private Set<CommentDto> comments;
}

