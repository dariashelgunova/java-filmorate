package ru.yandex.practicum.filmorate.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") Integer userId) {
        return userService.findById(userId);
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
        User friendInitializer = userService.findById(friendInitializerId);

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
