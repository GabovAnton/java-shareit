package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "item_proposals")
@RequiredArgsConstructor
public class ItemProposal {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long requesterId;

    @Column(nullable = false)
    private Long itemId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    @JsonIgnore
    private Request request;


}
