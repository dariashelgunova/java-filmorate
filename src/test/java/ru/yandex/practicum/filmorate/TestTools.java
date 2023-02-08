package ru.yandex.practicum.filmorate;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.db.dao.FilmDAO;
import ru.yandex.practicum.filmorate.storages.db.dao.UserDAO;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import static java.time.Instant.parse;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestTools {

    UserDAO userDAO;

    FilmDAO filmDAO;

    public void createUser1() {
        Date birthday = new Date(parse("2005-09-21T12:00:00Z").getLong(MILLI_OF_SECOND));
        userDAO.create(new User(null, "777@mail.ru", "login",
                "name", birthday, null));
    }

    public void createUser2() {
        Date birthday = new Date(parse("2003-10-20T12:00:00Z").getLong(MILLI_OF_SECOND));
        userDAO.create(new User(null, "888@mail.ru", "login2",
                "name2", birthday, null));
    }

    public void createUser3() {
        Date birthday = new Date(parse("2003-10-21T12:00:00Z").getLong(MILLI_OF_SECOND));
        userDAO.create(new User(null, "999@mail.ru", "login3",
                "name3", birthday, null));
    }

    public void createFilm1() {
        Date releaseDate = new Date(1220227200L * 1000);
        Genre genre = new Genre(1, "Комедия");
        Set<Genre> genres = new TreeSet<>();
        genres.add(genre);
        Mpa mpa = new Mpa(1, "G", "У фильма нет возрастных ограничений");
        Set<Integer> likes = new TreeSet<>();
        filmDAO.create(new Film(null, "new film", "film about zombies",
                releaseDate, 120, likes, 4, genres, mpa));
    }

}
