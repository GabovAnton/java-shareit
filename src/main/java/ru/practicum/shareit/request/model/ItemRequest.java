package ru.practicum.shareit.request.model;

import lombok.Data;
import org.hibernate.mapping.ToOne;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@Table(name = "item_request", schema = "public")
public class ItemRequest {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   private String description;
   @OneToOne
   @JoinColumn(name = "requestor_id")
   private User requestor;
   private LocalDateTime created;
}
