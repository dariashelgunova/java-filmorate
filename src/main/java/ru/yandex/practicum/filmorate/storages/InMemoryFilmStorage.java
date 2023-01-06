package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Primary
@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
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
        if (film.getId() != null)
            throw new ValidationException("Id создаваемого фильма не должен быть задан!");
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

    @NotNull
    private Film getByIdOrThrowException(int filmId) {
        Film film = films.get(filmId);
        if (film == null) throw new NotFoundObjectException("Объект не был найден!");
        return film;
    }
}
