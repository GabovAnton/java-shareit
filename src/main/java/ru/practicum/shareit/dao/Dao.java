package ru.practicum.shareit.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id);

    List<T> getAll();

    boolean save(T t);

    List<T> search(String query, long userId);

}
