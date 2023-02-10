package ru.yandex.practicum.filmorate.repos;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;

public interface MpaRepository {
    List<Mpa> findAll();
    Mpa findById(Integer id);
}
