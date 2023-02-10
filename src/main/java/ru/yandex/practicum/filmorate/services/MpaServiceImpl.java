package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.repos.MpaRepository;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    MpaRepository mpaRepository;

    @Override
    public List<Mpa> findAll() {
        return mpaRepository.findAll();
    }

    @Override
    public Mpa findById(Integer id) {
        return mpaRepository.findById(id);
    }

}
