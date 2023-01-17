package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

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
    public User findById(Integer id) {
        log.debug("Id пользователя - {}", id);
        return getUserByIdOrThrowException(id);
    }

    @Override
    public User create(User user) {
        log.debug("Объект - {}", user);

        if (isEmailAlreadyOccupied(user))
            throw new ValidationException("Данный адрес электронной почты уже присутствует в базе.");

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

        if (isEmailAlreadyOccupied(user))
            throw new ValidationException("Данный адрес электронной почты уже присутствует в базе.");

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

    private boolean isEmailAlreadyOccupied(User userToCheck) {
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
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }
}
