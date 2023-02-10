package ru.yandex.practicum.filmorate.repos;

import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findAll();
    Genre findById(Integer id);
}
