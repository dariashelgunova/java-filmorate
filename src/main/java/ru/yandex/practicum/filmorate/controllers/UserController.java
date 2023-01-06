package ru.yandex.practicum.filmorate.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.List;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserStorage userStorage;
    UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@Validated @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Validated @RequestBody User user) {
        return userStorage.update(user);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") Integer userId) {
        return userStorage.findById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void startFriendship(
            @PathVariable("id") Integer friendInitializerId,
            @PathVariable("friendId") Integer newFriendId
    ) {
        userService.startFriendship(friendInitializerId, newFriendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void endFriendship(
            @PathVariable("id") Integer friendInitializerId,
            @PathVariable("friendId") Integer newFriendId
    ) {
        userService.endFriendship(friendInitializerId, newFriendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable("id") Integer friendInitializerId) {
        User friendInitializer = userStorage.findById(friendInitializerId);

        return friendInitializer.getFriends();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> findCommonFriends(
            @PathVariable("id") Integer friendInitializerId,
            @PathVariable("otherId") Integer otherId
    ) {
        return userService.findCommonFriends(friendInitializerId, otherId);
    }
}
