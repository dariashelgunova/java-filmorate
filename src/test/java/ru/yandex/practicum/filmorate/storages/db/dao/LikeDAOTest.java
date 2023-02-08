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

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class LikeDAOTest {

    TestTools tools;
    LikeDAO likeDAO;

    @Test
    public void addLike() {
        tools.createUser1();
        tools.createFilm1();
        likeDAO.addLike(1, 1);

        assertEquals(1, likeDAO.getLikedUsersIdsByFilmId(1).size());
    }

    @Test
    public void deleteLike() {
        tools.createUser1();
        tools.createFilm1();
        likeDAO.addLike(1, 1);

        assertEquals(1, likeDAO.getLikedUsersIdsByFilmId(1).size());

        likeDAO.deleteLike(1, 1);
        assertEquals(0, likeDAO.getLikedUsersIdsByFilmId(1).size());
    }

    @Test
    void getLikedUsersIdsByFilmId() {
        tools.createFilm1();
        tools.createUser1();
        likeDAO.addLike(1, 1);

        List<Integer> actual = likeDAO.getLikedUsersIdsByFilmId(1);

        assertEquals(singletonList(1), actual);
    }
}