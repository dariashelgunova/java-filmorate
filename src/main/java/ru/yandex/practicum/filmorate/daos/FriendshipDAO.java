package ru.yandex.practicum.filmorate.daos;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface FriendshipDAO {
    void addFriendship(Integer user1Id, Integer user2Id);
    void deleteFriendship(Integer user1Id, Integer user2Id);
    List<User> findFriendsByUserId(Integer id);
    List<User> findCommonFriendsByUsersIds(int user1Id, int user2Id);
}
