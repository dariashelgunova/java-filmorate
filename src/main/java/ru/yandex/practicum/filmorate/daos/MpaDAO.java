package ru.yandex.practicum.filmorate.daos;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDAO {
    Optional<Mpa> findById(Integer Id);
    List<Mpa> findAll();
    Optional<Mpa> findMpaByFilmId(Integer filmId);
}
