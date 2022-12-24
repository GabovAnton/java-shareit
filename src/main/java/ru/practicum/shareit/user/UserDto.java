package ru.practicum.shareit.user;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotNull
    private String name;

    @NotBlank
    @Email
    private String email;

    @DateTimeFormat
    private String registrationDate;


}
