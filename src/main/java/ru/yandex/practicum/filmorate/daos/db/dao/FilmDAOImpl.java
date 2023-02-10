package ru.yandex.practicum.filmorate.daos.db.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.daos.FilmDAO;
import ru.yandex.practicum.filmorate.daos.db.rowmappers.FilmFieldsRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmDAOImpl implements FilmDAO {

    JdbcTemplate jdbcTemplate;
    FilmFieldsRowMapper filmFieldsRowMapper;

    @Override
    public Optional<Film> findById(Integer id) {
        return findFilmFieldsById(id);
    }

    private Optional<Film> findFilmFieldsById(Integer id) {
        String findFilmByIdSql =
                "select * " +
                "from film as f " +
                "left join mpa as m on f.mpa_id = m.id " +
                "where f.id = ?";
        List<Film> result = jdbcTemplate.query(findFilmByIdSql, filmFieldsRowMapper, id);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.get(0));
    }

    @Override
    public List<Film> findAll() {
        String findAllFilmsIdsSql =
                "select * " +
                "from film as f " +
                "left join mpa as m on f.mpa_id = m.id " +
                "order by f.id, f.rate desc ";
        return jdbcTemplate.query(findAllFilmsIdsSql, filmFieldsRowMapper);
    }
    @Override
    public List<Film> findBestFilms(int count) {
        String findAllFilmsIdsSql =
              "select * " +
              "from film as f " +
              "left join mpa as m on f.mpa_id = m.id " +
              "order by f.rate desc " +
              "limit ? ";
        return jdbcTemplate.query(findAllFilmsIdsSql, filmFieldsRowMapper, count);
    }

    @Override
    public Film create(Film film) {
        int generatedId = insertNewFilm(film);
        film.setId(generatedId);
        return film;
    }

    private int insertNewFilm(Film film) {
        String createFilmSql =
                "insert into  film (name, description, release_date, duration, mpa_id) " +
                        "values (?, ?, ?, ?, ?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement statement = connection.prepareStatement(createFilmSql, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            LocalDate releaseDate = film.getReleaseDate();
            statement.setDate(3, Date.valueOf(releaseDate));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        return requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Film update(Film film) {
        Optional<Film> filmFromDb = findById(film.getId());
        if (filmFromDb.isEmpty())
            throw new NotFoundObjectException(String.format("Пользователь с id %s не найден!", film.getId()));
        Film fromDb = filmFromDb.get();

        updateChangedFilmFields(film);
        return fromDb;
    }

    private void updateChangedFilmFields(Film film) {
        String updateFilmSql =
                "update film " +
                "set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "where id = ?";
        jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(updateFilmSql);
                statement.setString(1, film.getName());
                statement.setString(2, film.getDescription());
                LocalDate releaseDate = film.getReleaseDate();
                statement.setDate(3, Date.valueOf(releaseDate));
                statement.setInt(4, film.getDuration());
                statement.setInt(5, film.getMpa().getId());
                statement.setInt(6, film.getId());
                return statement;
            }
        );
    }
}
