package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exceptions.UniversalException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("userDbStorage")
    UserStorage userStorage;

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User findById(Integer id) {
        return getUserByIdOrThrowException(id);
    }

    @Override
    public User create(User user) {
        if (isEmailAlreadyOccupied(user))
            throw new ValidationException("Данный адрес электронной почты уже присутствует в базе.");

        return userStorage.create(setNameIfBlankAndGet(user));
    }

    @Override
    public User update(User user) {
        if (isEmailAlreadyOccupied(user))
            throw new ValidationException("Данный адрес электронной почты уже присутствует в базе.");

        return userStorage.update(user);
    }

    public void startFriendship(int friendInitializerId, int newFriendId) {
        User friendInitializer = getUserByIdOrThrowException(friendInitializerId);
        User newFriend = getUserByIdOrThrowException(newFriendId);

        if (friendInitializer.getFriends().contains(newFriend))
            throw new UniversalException("Данные пользователи уже дружат!");
        friendInitializer.getFriends().add(newFriend);

        userStorage.update(friendInitializer);
    }

    public void endFriendship(int friendInitializerId, int oldFriendId) {
        User friendInitializer = getUserByIdOrThrowException(friendInitializerId);
        User oldFriend = getUserByIdOrThrowException(oldFriendId);

        if (!friendInitializer.getFriends().contains(oldFriend))
            throw new UniversalException("Данные пользователи еще не дружат!");

        friendInitializer.getFriends().remove(oldFriend);
        oldFriend.getFriends().remove(friendInitializer);
        userStorage.update(friendInitializer);
        userStorage.update(oldFriend);
    }

    public Set<User> findCommonFriends(int user1Id, int user2Id) {
        User user1 = getUserByIdOrThrowException(user1Id);
        User user2 = getUserByIdOrThrowException(user2Id);

        List<User> user1Friends = user1.getFriends();
        List<User> user2Friends = user2.getFriends();

        return user1Friends.stream()
                .filter(user2Friends::contains)
                .collect(Collectors.toSet());
    }

    private User setNameIfBlankAndGet(User user) {
        if (isBlank(user.getName()))
            user.setName(user.getLogin());
        return user;
    }

    private boolean isEmailAlreadyOccupied(User userToCheck) {
        Map<Integer, User> users = userStorage.findAll().stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        String userToCheckEmail = userToCheck.getEmail();
        boolean isNew = true;
        if (userToCheck.getId() == null || users.containsKey(userToCheck.getId())) {
            for (User currentUser : users.values()) {
                if (currentUser.getEmail().equals(userToCheckEmail) &&
                        !Objects.equals(currentUser.getId(), userToCheck.getId())) {
                    isNew = false;
                    break;
                }
            }
        }
        return !isNew;
    }

    private User getUserByIdOrThrowException(int userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }
}
