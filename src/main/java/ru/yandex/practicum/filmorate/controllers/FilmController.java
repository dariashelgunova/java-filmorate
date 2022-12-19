package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/films")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer idCounter = 0;

    @GetMapping
    public List<Film> findAll() {
        log.debug("Пользователи - {}", films);

        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Validated @RequestBody Film film) {
        log.debug("Объект - {}", film);

        if (film.getId() != null && films.containsKey(film.getId())) {
            throw new ValidationException("Данный фильм уже присутствует в базе. Попробуйте другой метод.");
        } else {
            film.setId(++idCounter);
            films.put(idCounter, film);
            return film;
        }
    }

    @PutMapping
    public Film update(@Validated @RequestBody Film film) {
        log.debug("Объект - {}", film);

        if (film.getId() == null) {
            film.setId(++idCounter);
            films.put(idCounter, film);
            return film;
        } else if (films.containsKey(film.getId())) {
            Film currentFilm = films.get(film.getId());
            currentFilm.setName(film.getName());
            currentFilm.setDescription(film.getDescription());
            currentFilm.setReleaseDate(film.getReleaseDate());
            currentFilm.setDuration(film.getDuration());
            return currentFilm;
        } else {
            throw new ValidationException("Произошла ошибка при обработке запроса, попробуйте еще раз.");
        }
    }

}
