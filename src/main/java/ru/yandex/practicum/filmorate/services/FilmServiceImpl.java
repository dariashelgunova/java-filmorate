package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UniversalException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;
    public static final Comparator<Film> COMPARE_BY_LIKES =
            comparingInt((Film f) -> f.getLikes().size()).reversed();

    @Override
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film findById(Integer id) {
        return filmStorage.findById(id);
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public void addLike(int userId, int filmId) {
        User user = userStorage.findById(userId);
        Film film = filmStorage.findById(filmId);

        if (film.getLikes().contains(user))
            throw new UniversalException("Данный пользователь уже поставил лайк этому фильму!");

        film.getLikes().add(user);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        User user = userStorage.findById(userId);
        Film film = filmStorage.findById(filmId);

        if (!film.getLikes().contains(user))
            throw new UniversalException("Данный пользователь не поставил лайк этому фильму!");

        film.getLikes().remove(user);
    }

    @Override
    public List<Film> findBestFilms(int bestFilms) {
        return filmStorage.findAll().stream()
                .sorted(COMPARE_BY_LIKES)
                .limit(bestFilms)
                .collect(Collectors.toList());
    }
}
