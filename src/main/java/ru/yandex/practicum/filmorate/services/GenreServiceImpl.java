package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storages.GenreStorage;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenreServiceImpl implements GenreService {
    @Autowired
    GenreStorage genreStorage;

    @Override
    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    @Override
    public Genre findById(Integer id) {
        return getByIdOrThrowException(id);
    }

    private Genre getByIdOrThrowException(int genreId) {
        return genreStorage.findById(genreId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

}
