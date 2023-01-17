package ru.practicum.shareit.server.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.server.item.CommentDto;
import ru.practicum.shareit.server.item.ItemDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto implements Serializable {

    private Long id;

    private Set<ItemDto> items;

    private Set<CommentDto> comments;

    private String name;

    private String email;

    private String registrationDate;

    private void writeObject(ObjectOutputStream stream) throws IOException {

        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

        stream.defaultReadObject();
    }

}