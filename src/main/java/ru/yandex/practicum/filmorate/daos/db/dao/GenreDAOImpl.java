package ru.yandex.practicum.filmorate.daos.db.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.daos.GenreDAO;
import ru.yandex.practicum.filmorate.daos.db.rowmappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreDAOImpl implements GenreDAO {

    JdbcTemplate jdbcTemplate;
    GenreRowMapper genreRowMapper;

    @Override
    public Optional<Genre> findById(Integer genreId) {
        String findGenreSql =
                "select * " +
                "from genre " +
                "where id = ?";
        List<Genre> result = jdbcTemplate.query(findGenreSql, genreRowMapper, genreId);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.get(0));
    }

    @Override
    public List<Genre> findAll() {
        String findAllGenresSql =
                "select * " +
                "from genre" ;
        return jdbcTemplate.query(findAllGenresSql, genreRowMapper);
    }

    @Override
    public Set<Genre> findGenresByFilmId(Integer filmId) {
        Map<Integer, Genre> genresById = findAll().stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
        String findGenresIdsByFilmId =
                "select genre_id " +
                "from film_genre " +
                "where film_id = ?";
        return jdbcTemplate.queryForList(findGenresIdsByFilmId, Integer.class, filmId).stream()
                .map(genresById::get)
                .collect(Collectors.toSet());
    }

    public void updateFilmGenres(Film existingFilm, Film updatedFilm) {
        Set<Genre> existingGenres = existingFilm.getGenres();
        Set<Genre> updatedGenres = updatedFilm.getGenres();

        Set<Genre> addedGenres = new HashSet<>(updatedGenres);
        addedGenres.removeAll(existingGenres);

        Set<Genre> removedGenres = new HashSet<>(existingGenres);
        removedGenres.removeAll(updatedGenres);

        Integer filmId = existingFilm.getId();
        addGenresToFilm(addedGenres, filmId);
        removeGenresFromFilm(removedGenres, filmId);
    }

    public void removeGenresFromFilm(Collection<Genre> genres, Integer filmId) {
        if (genres == null) return;
        List<Genre> genresList = new ArrayList<>(genres);

        String addGenresSql =
                "delete from film_genre " +
                "where film_id = ? " +
                "  and genre_id = ?";

        jdbcTemplate.batchUpdate(addGenresSql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genresList.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                }
        );
    }

    public void addGenresToFilm(Collection<Genre> genres, Integer filmId) {
        if (genres == null) return;
        List<Genre> genresList = new ArrayList<>(genres);

        String addGenresSql =
                "merge into film_genre (film_id, genre_id) " +
                "values (?, ?)";

        jdbcTemplate.batchUpdate(addGenresSql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genresList.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                }
        );
    }
}
