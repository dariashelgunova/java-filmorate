package ru.yandex.practicum.filmorate.repos;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmRepository {

    List<Film> findAll();
    Film findById(Integer id);
    Film create(Film film);
    Film update(Film film);
    void addLike(int userId, int filmId);
    void deleteLike(int userId, int filmId);
    List<Film> findBestFilms(int bestFilms);
}
