package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDao {
    private final List<User> userList = new ArrayList<>();
    private long userId;


    public Optional<User> get(long id) {
        return userList.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    public List<User> getAll() {
        return userList;
    }

    public long save(User user) {
        checkEmailConstraints(user.getEmail());
        user.setId(++userId);
        userList.add(user);

        return user.getId();
    }


    public boolean removeUser(long userId) {
        userList.stream().filter(x -> x.getId() == userId)
                .findAny()
                .ifPresentOrElse(userList::remove, () -> {
                    throw new EntityNotFoundException("user with id: " + userId + " not found");
                });
        return true;
    }

    public void checkEmailConstraints(String email) {
        userList.stream()
                .filter(x -> x.getEmail().equals(email))
                .findAny()
                .ifPresent(x -> {
            throw new ConflictException("user with email: " + email + " already exists");
        });

    }

}
