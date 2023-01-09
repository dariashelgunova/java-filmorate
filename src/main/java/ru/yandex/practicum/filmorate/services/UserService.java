package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User findById(Integer id);
    List<User> findAll();
    User create(User user);
    User update(User user);
    void startFriendship(int friendInitializerId, int newFriendId);
    void endFriendship(int friendInitializerId, int oldFriendId);
    Set<User> findCommonFriends(int friend1Id, int friend2Id);
}
