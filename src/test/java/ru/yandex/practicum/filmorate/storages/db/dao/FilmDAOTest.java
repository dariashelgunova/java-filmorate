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
import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class FilmDAOTest {

    TestTools tools;

    FilmDAO filmDAO;

    @Test
    void findById() {
        tools.createFilm1();
        Optional<Film> filmOptional = filmDAO.findById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "new film")
                );
    }

    @Test
    void findAll() {
        tools.createFilm1();
        List<Film> films = filmDAO.findAll();

        assertEquals(1, films.size());
    }

    @Test
    void create() {
        tools.createFilm1();
        Optional<Film> filmOptional = filmDAO.findById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "new film")
                                .hasFieldOrPropertyWithValue("description", "film about zombies")
                );
    }

    @Test
    void update() {
        tools.createFilm1();
        Film updatedFilm = filmDAO.findById(1).orElseThrow();
        updatedFilm.setName("new name");
        updatedFilm.setDuration(30);

        filmDAO.update(updatedFilm);

        Optional<Film> filmOptional = filmDAO.findById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "new name")
                                .hasFieldOrPropertyWithValue("duration", 30)
                );
    }
}