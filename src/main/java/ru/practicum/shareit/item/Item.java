package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.Set;

@Table(name = "items")
@Entity
@Getter
@Setter
public class Item {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean isAvailable;

    @Column(nullable = false)
    private Integer requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "item")
    private Set<Booking> itemBookings;

    @OneToMany(mappedBy = "item")
    private Set<Comment> itemComments;

}





