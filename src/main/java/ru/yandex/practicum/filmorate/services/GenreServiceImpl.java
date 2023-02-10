package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.daos.GenreDAO;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    GenreDAO genreDAO;

    @Override
    public List<Genre> findAll() {
        return genreDAO.findAll();
    }

    @Override
    public Genre findById(Integer id) {
        return getByIdOrThrowException(id);
    }

    private Genre getByIdOrThrowException(int genreId) {
        return genreDAO.findById(genreId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }
}
