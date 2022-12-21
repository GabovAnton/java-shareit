package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "requests")
@RequiredArgsConstructor
public class Request {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 512)
    private String description;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    @JsonIgnore
    private User requester;

    @OneToMany(mappedBy = "request")
    private Set<ItemProposal> requestItemProposals;


}