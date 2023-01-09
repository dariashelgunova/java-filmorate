package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.Film;

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
    public Film findById(Integer id) {
        log.debug("Id  Фильма - {}", id);
        return getByIdOrThrowException(id);
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

        Film existingFilm = getByIdOrThrowException(filmId);
        return updateExistingFilm(existingFilm, film);
    }

    private Film updateExistingFilm(Film existingFilm, Film filmToUpdate) {
        existingFilm.setName(filmToUpdate.getName());
        existingFilm.setDescription(filmToUpdate.getDescription());
        existingFilm.setReleaseDate(filmToUpdate.getReleaseDate());
        existingFilm.setDuration(filmToUpdate.getDuration());
        return existingFilm;
    }

    private Film getByIdOrThrowException(int filmId) {
        return Optional.ofNullable(films.get(filmId))
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }
}
