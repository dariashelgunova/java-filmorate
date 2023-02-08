package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Optional<Genre> findById(Integer Id);
    List<Genre> findAll();
    void addGenreToFilm(Integer genreId, Integer filmId);
    void deleteGenreFromFilm(Integer genreId, Integer filmId);

    List<Integer> findGenresIdsByFilmId(Integer filmId);
}
