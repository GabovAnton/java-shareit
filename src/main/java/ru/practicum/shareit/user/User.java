package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   // @Column(name="CUST_ID")
    private long id;

    @Column(name="name", length=128, nullable=false, unique=true)
    private String name;

    @NotBlank
    @Email
    private String email;

    @Column(name = "registration_date",columnDefinition = "DATE")
    private LocalDate registrationDate; //yyyy.MM.dd, hh:mm:ss
}