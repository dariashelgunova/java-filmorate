package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Primary
@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public List<User> findAll() {
        log.debug("Пользователи - {}", users);
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Integer id) {
        log.debug("Id пользователя - {}", id);
        return getUserByIdOrThrowException(id);
    }

    @Override
    public User create(User user) {
        log.debug("Объект - {}", user);

        if (user.getId() != null)
            throw new ValidationException("Id создаваемого пользователя не должен быть задан!");

        if (isEmailAlreadyOccupied(user.getEmail()))
            throw new ValidationException("Данный адрес электронной почты уже присутствует в базе.");

        if (isBlank(user.getName()))
            user.setName(user.getLogin());

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

        User existingUser = getUserByIdOrThrowException(userId);
        return updateExistingUser(existingUser, user);
    }

    @NotNull
    private User updateExistingUser(User existingUser, User userToUpdate) {
        existingUser.setEmail(userToUpdate.getEmail());
        existingUser.setLogin(userToUpdate.getLogin());
        existingUser.setName(userToUpdate.getName());
        existingUser.setBirthday(userToUpdate.getBirthday());
        return existingUser;
    }

    private boolean isEmailAlreadyOccupied(String email) {
        boolean isNew = true;
        for (User currentUser : users.values()) {
            if (currentUser.getEmail().equals(email)) {
                isNew = false;
                break;
            }
        }
        return !isNew;
    }

    private User getUserByIdOrThrowException(int userId) {
        User user = users.get(userId);
        if (user == null) throw new NotFoundObjectException("Объект не был найден");
        return user;
    }

}
