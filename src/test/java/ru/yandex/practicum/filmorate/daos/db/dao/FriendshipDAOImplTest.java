package ru.yandex.practicum.filmorate.daos.db.dao;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class FriendshipDAOImplTest {

    TestTools tools;

    FriendshipDAOImpl friendshipDAOImpl;

    UserDAOImpl userDAOImpl;

    @Test
    void addFriendship() {
        tools.createUser1();
        tools.createUser2();

        friendshipDAOImpl.addFriendship(1, 2);
        List<User> actual = friendshipDAOImpl.findFriendsByUserId(1);

        assertEquals(1, actual.size());
    }

    @Test
    public void deleteFriendship() {
        tools.createUser1();
        tools.createUser2();
        friendshipDAOImpl.addFriendship(1, 2);

        friendshipDAOImpl.deleteFriendship(1, 2);
        List<User> actual = friendshipDAOImpl.findFriendsByUserId(1);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void findFriendshipByUserId() {
        tools.createUser1();
        tools.createUser2();
        tools.createUser3();

        friendshipDAOImpl.addFriendship(1, 2);
        friendshipDAOImpl.addFriendship(1, 3);

        List<User> friendsIdsByUserId = friendshipDAOImpl.findFriendsByUserId(1);

        assertEquals(2, friendsIdsByUserId.size());
    }
}