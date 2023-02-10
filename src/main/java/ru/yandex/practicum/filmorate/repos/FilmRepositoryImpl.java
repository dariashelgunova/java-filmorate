package ru.yandex.practicum.filmorate.repos;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.daos.FilmDAO;
import ru.yandex.practicum.filmorate.daos.GenreDAO;
import ru.yandex.practicum.filmorate.daos.UserDAO;
import ru.yandex.practicum.filmorate.daos.db.dao.LikeDAOImpl;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exceptions.UniversalException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {
    FilmDAO filmStorage;
    UserDAO userDAO;
    LikeDAOImpl likeDAOImpl;
    GenreDAO genreDAO;

    @Override
    public List<Film> findAll() {
        List<Film> all = filmStorage.findAll();
        for (Film film : all) {
            fillFilmGenresFor(film);
        }
        return all;
    }

    @Override
    public Film findById(Integer id) {
        Film film = getFilmByIdOrElseThrowException(id);
        fillFilmGenresFor(film);
        return film;
    }

    @Override
    public Film create(Film film) {
        Film created = filmStorage.create(film);

        genreDAO.addGenresToFilm(film.getGenres(), created.getId());
        fillFilmGenresFor(created);
        return created;
    }

    @Override
    public Film update(Film film) {
        Film updated = filmStorage.update(film);
        fillFilmGenresFor(updated);
        genreDAO.updateFilmGenres(updated, film);
        Film fromDb = getFilmByIdOrElseThrowException(film.getId());
        fillFilmGenresFor(fromDb);
        return fromDb;
    }

    @Override
    public void addLike(int userId, int filmId) {
        checkThatUserIsPresent(userId);
        getFilmByIdOrElseThrowException(filmId);

        List<Integer> likedFilmsIds = likeDAOImpl.findLikedFilmsByUser(userId);

        if (likedFilmsIds.contains(filmId))
            throw new UniversalException("Данный пользователь уже поставил лайк этому фильму!");

        likeDAOImpl.addLike(userId, filmId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        checkThatUserIsPresent(userId);
        getFilmByIdOrElseThrowException(filmId);

        List<Integer> likedFilmsIds = likeDAOImpl.findLikedFilmsByUser(userId);

        if (!likedFilmsIds.contains(filmId))
            throw new UniversalException("Данный пользователь не поставил лайк этому фильму!");

        likeDAOImpl.deleteLike(userId, filmId);
    }

    @Override
    public List<Film> findBestFilms(int bestFilms) {
        List<Film> films = filmStorage.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Film::getRate).reversed())
                .limit(bestFilms)
                .collect(Collectors.toList());
        for (Film film : films) {
            fillFilmGenresFor(film);
        }
        return films;
    }

    private Film getFilmByIdOrElseThrowException(int filmId) {
        return filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

    private void checkThatUserIsPresent(int userId) {
        userDAO.findById(userId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

    private void fillFilmGenresFor(Film film) {
        film.setGenres(genreDAO.findGenresByFilmId(film.getId()));
    }
}
