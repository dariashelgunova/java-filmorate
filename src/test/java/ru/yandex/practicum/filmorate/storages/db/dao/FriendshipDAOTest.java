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
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class FriendshipDAOTest {

    TestTools tools;

    FriendshipDAO friendshipDAO;

    UserDAO userDAO;

    @Test
    void addFriendship() {
        tools.createUser1();
        tools.createUser2();

        friendshipDAO.addFriendship(1, 2);

        List<User> user1Friends = new ArrayList<>();

        User user2 = userDAO.findById(2).orElse(null);
        user1Friends.add(user2);

        assertThat(userDAO.findById(1))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("friends", user1Friends));

        assertThat(userDAO.findById(2))
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("friends", emptyList()));
    }

    @Test
    public void deleteFriendship() {
        tools.createUser1();
        tools.createUser2();
        friendshipDAO.addFriendship(1, 2);

        friendshipDAO.deleteFriendship(1, 2);

        assertThat(userDAO.findById(1))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("friends", emptyList()));
    }

    @Test
    public void findFriendshipByUserId() {
        tools.createUser1();
        tools.createUser2();
        tools.createUser3();

        friendshipDAO.addFriendship(1, 2);
        friendshipDAO.addFriendship(1, 3);

        List<Integer> friendsIdsByUserId = friendshipDAO.findFriendsIdsByUserId(1);

        assertEquals(2, friendsIdsByUserId.size());
    }
}