package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.Nullable;
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
import static java.util.Optional.ofNullable;

@Primary
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;
    private static final int DEFAULT_BEST_FILMS = 10;
    public static final Comparator<Film> COMPARE_BY_LIKES =
            comparingInt((Film f) -> f.getLikes().size()).reversed();

    public void addLike(int userId, int filmId) {
        User user = userStorage.findById(userId);
        Film film = filmStorage.findById(filmId);

        if (film.getLikes().contains(user))
            throw new UniversalException("Данный пользователь уже поставил лайк этому фильму!");

        film.getLikes().add(user);
    }

    public void deleteLike(int userId, int filmId) {
        User user = userStorage.findById(userId);
        Film film = filmStorage.findById(filmId);

        if (!film.getLikes().contains(user))
            throw new UniversalException("Данный пользователь не поставил лайк этому фильму!");

        film.getLikes().remove(user);
    }

    public List<Film> findBestFilms(@Nullable Integer bestFilms) {
        int films = ofNullable(bestFilms).orElse(DEFAULT_BEST_FILMS);
        return getBestFilms(films);
    }

    private List<Film> getBestFilms(int films) {
        return filmStorage.findAll().stream()
                .sorted(COMPARE_BY_LIKES)
                .limit(films)
                .collect(Collectors.toList());
    }
}
