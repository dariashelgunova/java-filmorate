package ru.yandex.practicum.filmorate.daos;

import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreDAO {
    Optional<Genre> findById(Integer genreId);
    List<Genre> findAll();
    Set<Genre> findGenresByFilmId(Integer filmId);
    void updateFilmGenres(Film existingFilm, Film updatedFilm);
    void addGenresToFilm(Collection<Genre> genres, Integer filmId);
    void load(List<Film> films);
}
