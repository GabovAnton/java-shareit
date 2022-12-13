package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotNull
    private String name;

    @NotBlank
    @Email
    private String email;

    @Email
    private String registrationDate;


}
