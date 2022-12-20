package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.Request;

import javax.persistence.*;
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
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "owner")
    private Set<Item> items;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Comment> Comments;

    @OneToMany(mappedBy = "requester")
    private Set<Request> requesterRequests;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, length = 512, unique = true)
    private String email;
}