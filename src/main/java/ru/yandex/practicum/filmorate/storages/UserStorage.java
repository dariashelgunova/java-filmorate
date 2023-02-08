package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> findById(Integer id);
    List<User> findAll();
    User create(User user);
    User update(User user);
}
