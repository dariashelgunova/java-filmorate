package ru.yandex.practicum.filmorate.validationTests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer idCounter = 0;

    @GetMapping
    public List<User> findAll() {
        log.debug("Пользователи - {}", users);

        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Validated @RequestBody User user) {
        log.debug("Объект - {}", user);

        if (user.getId() != null && users.containsKey(user.getId())) {
            throw new ValidationException("Данный пользователь уже присутствует в базе. Попробуйте другой метод.");
        } else if (!isEmailNew(user.getEmail())) {
            throw new ValidationException("Данный адрес электронной почты уже присутствует в базе.");
        } else {
            if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
            user.setId(++idCounter);
            users.put(idCounter, user);
            return user;
        }
    }

    @PutMapping
    public User update(@Validated @RequestBody User user) {
        log.debug("Объект - {}", user);

        if (user.getId() == null) {
            create(user);
            user.setId(++idCounter);
            users.put(idCounter, user);
            return user;
        } else if (users.containsKey(user.getId())) {
            User currentUser = users.get(user.getId());
            currentUser.setEmail(user.getEmail());
            currentUser.setLogin(user.getLogin());
            currentUser.setName(user.getName());
            currentUser.setBirthday(user.getBirthday());
            return currentUser;
        } else {
            throw new ValidationException("Произошла ошибка при обработке запроса, попробуйте еще раз.");
        }

    }

    private boolean isEmailNew (String email) {
        boolean isNew = true;
        for (User currentUser : users.values()) {
            if (currentUser.getEmail().equals(email)) {
                isNew = false;
                break;
            }
        }
        return isNew;
    }
}
