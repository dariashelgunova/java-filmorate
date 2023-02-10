package ru.yandex.practicum.filmorate.repos;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.daos.FriendshipDAO;
import ru.yandex.practicum.filmorate.daos.UserDAO;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exceptions.UniversalException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    UserDAO userDAO;
    FriendshipDAO friendshipDAO;

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findById(Integer id) {
        return getUserByIdOrThrowException(id);
    }

    @Override
    public User create(User user) {
        return userDAO.create(setNameIfBlankAndGet(user));
    }

    @Override
    public User update(User user) {
        return userDAO.update(user);
    }

    @Override
    public void startFriendship(int friendInitializerId, int newFriendId) {
        User newFriend = getUserByIdOrThrowException(newFriendId);

        if (friendshipDAO.findFriendsByUserId(friendInitializerId).contains(newFriend)) {
            throw new UniversalException("Данные пользователи уже дружат!");
        }
        friendshipDAO.addFriendship(friendInitializerId, newFriendId);
    }

    @Override
    public void endFriendship(int friendInitializerId, int friendIdToDelete) {
        User oldFriend = getUserByIdOrThrowException(friendIdToDelete);

        List<User> userFriends = friendshipDAO.findFriendsByUserId(friendInitializerId);
        if (!userFriends.contains(oldFriend)){
            throw new UniversalException("Данные пользователи еще не дружат!");
        }

        friendshipDAO.deleteFriendship(friendInitializerId, friendIdToDelete);
        friendshipDAO.deleteFriendship(friendIdToDelete, friendInitializerId);
    }

    @Override
    public List<User> findCommonFriends(int user1Id, int user2Id) {
        return friendshipDAO.findCommonFriendsByUsersIds(user1Id, user2Id);
    }

    private User setNameIfBlankAndGet(User user) {
        if (isBlank(user.getName()))
            user.setName(user.getLogin());
        return user;
    }

    private User getUserByIdOrThrowException(int userId) {
        return userDAO.findById(userId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

    @Override
    public List<User> findFriendsByUserId(Integer id) {
        return friendshipDAO.findFriendsByUserId(id);
    }
}
