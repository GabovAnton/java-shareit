package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @OneToMany(mappedBy = "owner")
    private Set<Item> items;

    @OneToMany(mappedBy = "booker")
    private Set<Booking> Bookings;

    @OneToMany(mappedBy = "author")
    private Set<Comment> Comments;

    @OneToMany(mappedBy = "requestor")
    private Set<ItemRequest> Requests;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="email",nullable=false,  length = 512, unique=true)
    private String email;

    @Column(name = "registration_date",columnDefinition = "DATE")
    private LocalDate registrationDate; //yyyy.MM.dd, hh:mm:ss
}