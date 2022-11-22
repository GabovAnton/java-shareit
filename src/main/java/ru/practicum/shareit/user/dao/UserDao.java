package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDao {
    private final List<User> userList = new ArrayList<>();
    private long userId;


    public Optional<User> get(long id) {
        return userList.stream().filter(x -> x.getId() == id).findFirst();
    }

    public List<User> getAll() {
        return userList;
    }

    public long save(User user) {
        user.setId(++userId);
        validateUser(user);
        userList.stream().filter(x -> x.equals(user) || x.getEmail().equals(user.getEmail())).findAny().
                ifPresentOrElse(x -> {
                            throw new ValidationException("user can't be created because it already exists");
                        },
                        () -> userList.add(user));
        return user.getId();
    }

    private void validateUser(User user) {

    }

}
