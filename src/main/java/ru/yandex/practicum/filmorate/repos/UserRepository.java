package ru.yandex.practicum.filmorate.repos;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserRepository {
    User findById(Integer id);
    List<User> findAll();
    User create(User user);
    User update(User user);
    void startFriendship(int friendInitializerId, int newFriendId);
    void endFriendship(int friendInitializerId, int oldFriendId);
    List<User> findCommonFriends(int friend1Id, int friend2Id);
    List<User> findFriendsByUserId(Integer id);
}
