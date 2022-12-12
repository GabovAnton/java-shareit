package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @OneToMany(mappedBy = "owner")
    private Set<Item> items;



    @OneToMany(mappedBy = "author")
    private Set<Comment> Comments;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="email",nullable=false,  length = 512, unique=true)
    private String email;

    @Column(name = "registration_date",columnDefinition = "DATE")
    private LocalDate registrationDate; //yyyy.MM.dd, hh:mm:ss
}