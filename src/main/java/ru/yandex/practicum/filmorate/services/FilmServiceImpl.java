package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.repos.FilmRepository;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    FilmRepository filmRepository;

    @Override
    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Film findById(Integer id) {
        return filmRepository.findById(id);
    }

    @Override
    public Film create(Film film) {
        return filmRepository.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmRepository.update(film);
    }

    @Override
    public void addLike(int userId, int filmId) {
        filmRepository.addLike(userId, filmId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        filmRepository.deleteLike(userId, filmId);
    }

    @Override
    public List<Film> findBestFilms(int bestFilms) {
        return filmRepository.findBestFilms(bestFilms);
    }
}
