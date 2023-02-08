package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.storages.MpaStorage;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MpaServiceImpl implements MpaService {
    @Autowired
    MpaStorage mpaStorage;

    @Override
    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    @Override
    public Mpa findById(Integer id) {
        return getByIdOrThrowException(id);
    }

    private Mpa getByIdOrThrowException(int mpaId) {
        return mpaStorage.findById(mpaId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }
}
