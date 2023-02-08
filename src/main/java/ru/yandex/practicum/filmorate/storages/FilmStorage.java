package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> findById(Integer Id);
    List<Film> findAll();
    Film create(Film film);
    Film update(Film film);
}
