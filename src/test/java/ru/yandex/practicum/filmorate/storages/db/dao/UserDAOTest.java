package ru.yandex.practicum.filmorate.storages.db.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.TestTools;
import ru.yandex.practicum.filmorate.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserDAOTest {

    TestTools tools;
    UserDAO userStorage;

    @Test
    void findById() {
        tools.createUser1();
        Optional<User> userOptional = userStorage.findById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void findAll() {
        tools.createUser1();
        tools.createUser2();

        List<User> users = userStorage.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void create() {
        tools.createUser1();
        Optional<User> userOptional = userStorage.findById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "777@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "login")
                );
    }

    @Test
    void update() {
        tools.createUser1();
        tools.createUser2();

        List<User> friends = new ArrayList<>();
        User friend = userStorage.findById(2).orElseThrow();
        friends.add(friend);
        Date birthday = new Date(1220227200L * 1000);
        User newUser = new User(1, "888@mail.ru", "login",
                "name", birthday, friends);
        userStorage.update(newUser);

        Optional<User> userOptional = userStorage.findById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "888@mail.ru")
                                .hasFieldOrPropertyWithValue("friends", friends)
                );
    }
}