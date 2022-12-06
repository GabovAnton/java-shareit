package ru.practicum.shareit.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.ToOne;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Table(name = "item_request")
@Getter
@Setter
public class ItemRequest {

   @Id
   @Column(nullable = false, updatable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String description;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "requestor_id", nullable = false)
   private User requestor;

}
