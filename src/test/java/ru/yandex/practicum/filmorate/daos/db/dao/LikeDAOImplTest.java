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

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class LikeDAOImplTest {

    TestTools tools;
    LikeDAOImpl likeDAOImpl;

    @Test
    public void addLike() {
        tools.createUser1();
        tools.createFilm1();
        likeDAOImpl.addLike(1, 1);

        assertEquals(1, likeDAOImpl.findLikedFilmsByUser(1).size());
    }

    @Test
    public void deleteLike() {
        tools.createUser1();
        tools.createFilm1();
        likeDAOImpl.addLike(1, 1);

        assertEquals(1, likeDAOImpl.findLikedFilmsByUser(1).size());

        likeDAOImpl.deleteLike(1, 1);
        assertTrue(likeDAOImpl.findLikedFilmsByUser(1).isEmpty());
    }

    @Test
    void getLikedUsersIdsByFilmId() {
        tools.createFilm1();
        tools.createUser1();
        likeDAOImpl.addLike(1, 1);

        List<Integer> actual = likeDAOImpl.findLikedFilmsByUser(1);

        assertEquals(singletonList(1), actual);
    }
}