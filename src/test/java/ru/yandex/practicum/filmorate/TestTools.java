package ru.yandex.practicum.filmorate;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.daos.db.dao.FilmDAOImpl;
import ru.yandex.practicum.filmorate.daos.db.dao.GenreDAOImpl;
import ru.yandex.practicum.filmorate.daos.db.dao.UserDAOImpl;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestTools {

    UserDAOImpl userDAOImpl;

    FilmDAOImpl filmDAOImpl;

    GenreDAOImpl genreDAOImpl;

    public void createUser1() {
        LocalDate birthday = LocalDate.of(2005, 9, 21);

        userDAOImpl.create(new User(null, "777@mail.ru", "login", "name", birthday));
    }

    public void createUser2() {
        LocalDate birthday = LocalDate.of(2003, 10, 20);
        userDAOImpl.create(new User(null, "888@mail.ru", "login2", "name2", birthday));
    }

    public void createUser3() {
        LocalDate birthday = LocalDate.of(2003, 10, 21);
        userDAOImpl.create(new User(null, "999@mail.ru", "login3", "name3", birthday));
    }

    public void createFilm1() {
        LocalDate releaseDate = LocalDate.of(2005, 10, 15);
        Genre genre = new Genre(1, "Комедия");
        Set<Genre> genres = new TreeSet<>();
        genres.add(genre);
        Mpa mpa = new Mpa(1, "G", "У фильма нет возрастных ограничений");
        Film film = filmDAOImpl.create(new Film(null, "new film", "film about zombies",
                releaseDate, 120, 4, genres, mpa));
        genreDAOImpl.addGenresToFilm(genres, film.getId());
    }
}
