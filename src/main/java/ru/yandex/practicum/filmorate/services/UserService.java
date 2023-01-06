package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.models.User;

import java.util.Set;

public interface UserService {
    void startFriendship(int friendInitializerId, int newFriendId);
    void endFriendship(int friendInitializerId, int oldFriendId);
    Set<User> findCommonFriends(int friend1Id, int friend2Id);
}
