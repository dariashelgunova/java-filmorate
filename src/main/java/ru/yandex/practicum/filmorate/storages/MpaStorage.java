package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    Optional<Mpa> findById(Integer Id);
    List<Mpa> findAll();
    Optional<Mpa> findMpaByFilmId(Integer filmId);
}
