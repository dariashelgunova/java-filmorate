package ru.yandex.practicum.filmorate.daos.db.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.daos.MpaDAO;
import ru.yandex.practicum.filmorate.daos.db.rowmappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MpaDAOImpl implements MpaDAO {

    JdbcTemplate jdbcTemplate;
    MpaRowMapper mpaRowMapper;

    @Override
    public Optional<Mpa> findById(Integer mpaId) {
        String findMpaSql =
                "select * " +
                "from mpa " +
                "where id = ?";
        List<Mpa> result = jdbcTemplate.query(findMpaSql, mpaRowMapper, mpaId);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.get(0));
    }

    @Override
    public List<Mpa> findAll() {
        String findAllGenreIdsSql =
                "select id " +
                "from mpa" ;
        List<Integer> allMpaIds = jdbcTemplate.queryForList(findAllGenreIdsSql, Integer.class);
        List<Mpa> allMpas = new ArrayList<>();

        for (Integer id : allMpaIds) {
            Optional<Mpa> byId = findById(id);
            if (byId.isEmpty())
                throw new RuntimeException("Возникла внутренняя ошибка...");
            allMpas.add(byId.get());
        }
        return allMpas;
    }

    @Override
    public Optional<Mpa> findMpaByFilmId(Integer filmId) {
        String findMpaSql =
                "select mpa.* " +
                "from film " +
                "  left join mpa on film.mpa_id = mpa.id " +
                "where film.id = ?";
        List<Mpa> result = jdbcTemplate.query(findMpaSql, mpaRowMapper, filmId);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.get(0));
    }
}
