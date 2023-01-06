package ru.yandex.practicum.filmorate.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.util.List;

@RestController
@Validated
@RequestMapping("/films")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmController {
    FilmStorage filmStorage;
    FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @PostMapping
    public Film create(@Validated @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Validated @RequestBody Film film) {
        return filmStorage.update(film);
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") Integer filmId) {
        return filmStorage.findById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmId,
                        @PathVariable("userId") Integer userId) {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId,
                           @PathVariable("userId") Integer userId) {
        filmService.deleteLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> findBestFilms(@RequestParam(required = false) Integer count) {
        return filmService.findBestFilms(count);
    }
}
