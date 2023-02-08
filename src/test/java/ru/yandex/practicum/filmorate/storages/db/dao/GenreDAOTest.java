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
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.*;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class GenreDAOTest {

    TestTools tools;
    GenreDAO genreDAO;
    FilmDAO filmDAO;

    @Test
    public void addGenreToFilm() {
        tools.createFilm1();

        genreDAO.addGenreToFilm(2, 1);

        Set<Genre> expectedGenres =
                new TreeSet<>(Arrays.asList(
                        new Genre(1, "Комедия"),
                        new Genre(2, "Драма")
                ));
        assertThat(filmDAO.findById(1))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("genres", expectedGenres));
    }

    @Test
    public void deleteGenreToFilm() {
        tools.createFilm1();

        genreDAO.deleteGenreFromFilm(1, 1);

        assertThat(filmDAO.findById(1))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("genres", emptySet()));
    }

    @Test
    void findById() {
        assertThat(genreDAO.findById(1))
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия"));
    }

    @Test
    void findAll() {
        List<Genre> all = genreDAO.findAll();

        assertEquals(6, all.size());
    }

    @Test
    void findGenresIdsByFilmId() {
        tools.createFilm1();

        List<Integer> actual = genreDAO.findGenresIdsByFilmId(1);

        assertEquals(singletonList(1), actual);
    }
}