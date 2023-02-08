package ru.yandex.practicum.filmorate.storages.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import javax.validation.constraints.NotNull;
import java.util.*;


@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public List<User> findAll() {
        log.debug("Пользователи - {}", users);
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Integer id) {
        log.debug("Id пользователя - {}", id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        log.debug("Объект - {}", user);
        return createNewUser(user);
    }

    private User createNewUser(User user) {
        user.setId(++idCounter);
        users.put(idCounter, user);
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("Объект - {}", user);
        Integer userId = user.getId();
        if (userId == null)
            return create(user);

        User existingUser = users.get(userId);
        if (existingUser == null)
            throw new NotFoundObjectException("Объект не был найден");

        return updateExistingUser(existingUser, user);
    }

    @NotNull
    private User updateExistingUser(User existingUser, User userToUpdate) {
        existingUser.setEmail(userToUpdate.getEmail());
        existingUser.setLogin(userToUpdate.getLogin());
        existingUser.setName(userToUpdate.getName());
        existingUser.setBirthday(userToUpdate.getBirthday());
        existingUser.setFriends(userToUpdate.getFriends());
        return existingUser;
    }
}
