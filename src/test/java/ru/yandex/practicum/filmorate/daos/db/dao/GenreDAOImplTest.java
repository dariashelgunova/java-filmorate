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
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class GenreDAOImplTest {

    TestTools tools;
    GenreDAOImpl genreDAOImpl;

    @Test
    public void addGenreToFilm() {
        tools.createFilm1();

        genreDAOImpl.addGenresToFilm(singletonList(new Genre(2, "Драма")), 1);
        Set<Genre> actual = genreDAOImpl.findGenresByFilmId(1);

        assertEquals(2, actual.size());
    }

    @Test
    void findById() {
        assertThat(genreDAOImpl.findById(1))
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия"));
    }

    @Test
    void findAll() {
        List<Genre> all = genreDAOImpl.findAll();

        assertEquals(6, all.size());
    }

    @Test
    void findGenresIdsByFilmId() {
        tools.createFilm1();

        Set<Genre> actual = genreDAOImpl.findGenresByFilmId(1);

        assertEquals(singleton(new Genre(1, "Комедия")), actual);
    }

}