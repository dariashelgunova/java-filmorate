package ru.yandex.practicum.filmorate.repos;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.daos.MpaDAO;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MpaRepositoryImpl implements MpaRepository {
    MpaDAO mpaDAO;

    @Override
    public List<Mpa> findAll() {
        return mpaDAO.findAll();
    }

    @Override
    public Mpa findById(Integer id) {
        return getByIdOrThrowException(id);
    }

    private Mpa getByIdOrThrowException(int mpaId) {
        return mpaDAO.findById(mpaId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }
}
