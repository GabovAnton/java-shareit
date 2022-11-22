package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.dao.Dao;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDao {
    private final List<User> userList = new ArrayList<>();
    private long userId;


    public Optional<User> get(long id) {
        return userList.stream().filter(x -> x.getId() == id).findFirst();
    }

    public List<User> getAll() {
        return  userList;
    }

    public boolean save(User user) {
        user.setId(userId++);
        if (userList.stream().anyMatch(x -> x.equals(user))) return false;
        userList.add(user);
        return true;
    }

    public List<User> search(String query,long userId) {
        //TODO insert correct algorithm
        return userList.
                stream().
                filter(x -> x.getName().contains(query) && x.getId() != userId).
                collect(Collectors.toList());
    }
}
