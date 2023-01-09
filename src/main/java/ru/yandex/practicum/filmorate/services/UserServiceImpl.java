package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UniversalException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserStorage userStorage;

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userStorage.findById(id);
    }

    @Override
    public User create(User user) {
        return userStorage.create(setNameIfBlankAndGet(user));
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    public void startFriendship(int friendInitializerId, int newFriendId) {
        User friendInitializer = userStorage.findById(friendInitializerId);
        User newFriend = userStorage.findById(newFriendId);

        if (friendInitializer.getFriends().contains(newFriend))
            throw new UniversalException("Данные пользователи уже дружат!");

        friendUsers(friendInitializer, newFriend);
    }

    private void friendUsers(User friend1, User friend2) {
        friend1.getFriends().add(friend2);
        friend2.getFriends().add(friend1);
    }

    public void endFriendship(int friendInitializerId, int oldFriendId) {
        User friendInitializer = userStorage.findById(friendInitializerId);
        User oldFriend = userStorage.findById(oldFriendId);

        if (!friendInitializer.getFriends().contains(oldFriend))
            throw new UniversalException("Данные пользователи еще не дружат!");

        unfriendUsers(friendInitializer, oldFriend);
    }

    private void unfriendUsers(User friendInitializer, User oldFriend) {
        friendInitializer.getFriends().remove(oldFriend);
        oldFriend.getFriends().remove(friendInitializer);
    }

    public Set<User> findCommonFriends(int user1Id, int user2Id) {
        User user1 = userStorage.findById(user1Id);
        User user2 = userStorage.findById(user2Id);

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
}
