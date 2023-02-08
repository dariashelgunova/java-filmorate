package ru.yandex.practicum.filmorate.storages.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public List<Film> findAll() {
        log.debug("Фильмы - {}", films);
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(Integer id) {
        log.debug("Id  Фильма - {}", id);
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film create(Film film) {
        log.debug("Объект - {}", film);
        return createNewFilm(film);
    }

    private Film createNewFilm(Film film) {
        film.setId(++idCounter);
        films.put(idCounter, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        log.debug("Объект - {}", film);

        Integer filmId = film.getId();
        if (filmId == null) return create(film);

        Film existingFilm = films.get(filmId);
        if (existingFilm == null) {
            throw new NotFoundObjectException("Объект не был найден");
        }
        return updateExistingFilm(existingFilm, film);
    }

    private Film updateExistingFilm(Film existingFilm, Film filmToUpdate) {
        existingFilm.setName(filmToUpdate.getName());
        existingFilm.setDescription(filmToUpdate.getDescription());
        existingFilm.setReleaseDate(filmToUpdate.getReleaseDate());
        existingFilm.setDuration(filmToUpdate.getDuration());
        existingFilm.setGenres(filmToUpdate.getGenres());
        existingFilm.setRate(filmToUpdate.getRate());
        return existingFilm;
    }
}
