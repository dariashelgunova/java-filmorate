package ru.yandex.practicum.filmorate.storages.db.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storages.GenreStorage;
import ru.yandex.practicum.filmorate.storages.db.rowmappers.GenreRowMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreDAO implements GenreStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public void addGenreToFilm(Integer genreId, Integer filmId) {
        String addGenreSql =
                "insert into film_genre (film_id, genre_id) " +
                "values (?, ?)";
        jdbcTemplate.update(addGenreSql, filmId, genreId);
    }

    @Override
    public void deleteGenreFromFilm(Integer genreId, Integer filmId) {
        String deleteGenreSql =
                "delete from film_genre " +
                "where film_id = ? " +
                "and genre_id = ?";
        jdbcTemplate.update(deleteGenreSql, filmId, genreId);
    }

    @Override
    public Optional<Genre> findById(Integer genreId) {
        String findGenreSql =
                "select * " +
                "from genre " +
                "where id = ?";
        List<Genre> result = jdbcTemplate.query(findGenreSql, new GenreRowMapper(), genreId);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.get(0));
    }

    @Override
    public List<Genre> findAll() {
        String findAllGenreIdsSql =
                "select id " +
                "from genre" ;
        List<Integer> allGenresIds = jdbcTemplate.queryForList(findAllGenreIdsSql, Integer.class);
        List<Genre> allGenres = new ArrayList<>();

        for (Integer id : allGenresIds) {
            Optional<Genre> byId = findById(id);
            if (byId.isEmpty())
                throw new RuntimeException("Возникла внутренняя ошибка...");
            allGenres.add(byId.get());
        }
        allGenres.sort(Comparator.comparingInt(Genre::getId));
        return allGenres;
    }

    @Override
    public List<Integer> findGenresIdsByFilmId(Integer filmId) {
        String findFilmGenresSql =
                "select genre_id " +
                "from film_genre " +
                "where film_id = ?";
        return jdbcTemplate.queryForList(findFilmGenresSql, Integer.class, filmId);
    }
}
