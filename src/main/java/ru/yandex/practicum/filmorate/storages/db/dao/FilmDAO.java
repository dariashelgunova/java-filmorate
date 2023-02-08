package ru.yandex.practicum.filmorate.storages.db.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.db.rowmappers.FilmFieldsRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmDAO implements FilmStorage {

    JdbcTemplate jdbcTemplate;
    LikeDAO likeDAO;
    GenreDAO genreDAO;
    MpaDAO mpaDAO;


    @Override
    public Optional<Film> findById(Integer id) {
        Optional<Film> filmById = findFilmFieldsById(id);
        if (filmById.isEmpty()) return Optional.empty();

        Film film = filmById.get();
        addLikes(film);
        addGenres(film);
        addMpa(film);
        return Optional.of(film);
    }

    private void addMpa(Film film) {
        Optional<Mpa> mpa = mpaDAO.findMpaByFilmId(film.getId());
        if (mpa.isEmpty())
            throw new RuntimeException("Возникла внутренняя ошибка...");
        film.setMpa(mpa.get());
    }

    private Optional<Film> findFilmFieldsById(Integer id) {
        String findFilmByIdSql =
                "select * " +
                "from film " +
                "where id = ?";
        List<Film> result = jdbcTemplate.query(findFilmByIdSql, new FilmFieldsRowMapper(), id);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.get(0));
    }

    private void addLikes(Film film) {
        Set<Integer> likedUsersIds = new TreeSet<>(likeDAO.getLikedUsersIdsByFilmId(film.getId()));
        film.setLikes(likedUsersIds);
    }

    private void addGenres(Film film) {
        Set<Integer> genresIds = new TreeSet<>(genreDAO.findGenresIdsByFilmId(film.getId()));
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));

        for (Integer genreId : genresIds) {
            Optional<Genre> byId = genreDAO.findById(genreId);
            if (byId.isEmpty())
                throw new RuntimeException("Возникла внутренняя ошибка...");
            genres.add(byId.get());
        }
        film.setGenres(genres);
    }

    @Override
    public List<Film> findAll() {
        String findAllFilmsIdsSql =
                "select id " +
                "from film";
        List<Integer> allFilmsIds = jdbcTemplate.queryForList(findAllFilmsIdsSql, Integer.class);
        List<Film> allFilms = new ArrayList<>();

        for (Integer filmId : allFilmsIds) {
            Optional<Film> byId = findById(filmId);
            if (byId.isEmpty())
                throw new RuntimeException("Возникла внутренняя ошибка...");
            allFilms.add(byId.get());
        }
        allFilms.sort(Comparator.comparingInt(Film::getId));
        return allFilms;
    }
    @Override
    public Film create(Film film) {
        int generatedId = insertNewFilm(film);
        film.setId(generatedId);
        addDataAboutGenres(film);
        addDataAboutLikes(film);

        return film;
    }

    private int insertNewFilm(Film film) {
        String createFilmSql =
                "insert into  film (name, description, release_date, duration, rate, mpa_id) " +
                        "values (?, ?, ?, ?, ?, ?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement statement = connection.prepareStatement(createFilmSql, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, new Date(film.getReleaseDate().getTime()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getRate());
            statement.setInt(6, film.getMpa().getId());
            return statement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        return requireNonNull(keyHolder.getKey()).intValue();
    }

    private void addDataAboutGenres(Film film) {
        if (film.getGenres() == null) return;

        String addGenresSql =
                "insert into film_genre (film_id, genre_id) " +
                        "values (?, ?)";

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(addGenresSql, film.getId(), genre.getId());
        }
    }

    private void addDataAboutLikes(Film film) {
        if (film.getLikes() == null) return;

        String addLikesSql =
                "insert into likes (user_id, film_id) " +
                        "values (?, ?)";

        for (Integer userId : film.getLikes()) {
            jdbcTemplate.update(addLikesSql, userId, film.getId());
        }
    }
    
    @Override
    public Film update(Film film) {
        Optional<Film> filmFromDb = findById(film.getId());
        if (filmFromDb.isEmpty())
            throw new NotFoundObjectException(String.format("Пользователь с id %s не найден!", film.getId()));
        Film fromDb = filmFromDb.get();

        updateChangedFilmFields(film);
        updateLikes(fromDb, film);
        updateGenres(fromDb, film);
        return film;
    }

    private void updateChangedFilmFields(Film film) {
        String updateFilmSql =
                "update film " +
                "set name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? " +
                "where id = ?";
        jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(updateFilmSql);
                statement.setString(1, film.getName());
                statement.setString(2, film.getDescription());
                statement.setDate(3, new Date(film.getReleaseDate().getTime()));
                statement.setInt(4, film.getDuration());
                statement.setInt(5, film.getRate());
                statement.setInt(6, film.getMpa().getId());
                statement.setInt(7, film.getId());
                return statement;
            }
        );
    }

    private void updateLikes(Film existingFilm, Film updatedFilm) {
        Set<Integer> existingLikes = existingFilm.getLikes();
        Set<Integer> updatedLikes = updatedFilm.getLikes();

        Set<Integer> addedLikes = new TreeSet<>(updatedLikes);
        addedLikes.removeAll(existingLikes);

        Set<Integer> removedLikes = new TreeSet<>(existingLikes);
        removedLikes.removeAll(updatedLikes);

        for (Integer addedLike : addedLikes) {
            likeDAO.addLike(addedLike, updatedFilm.getId());
        }

        for (Integer removedLike : removedLikes) {
            likeDAO.deleteLike(removedLike, updatedFilm.getId());
        }
    }

    private void updateGenres(Film existingFilm, Film updatedFilm) {
        Set<Genre> existingGenres = existingFilm.getGenres();
        Set<Integer> existingGenresIds = new TreeSet<>();
        for (Genre genre : existingGenres) {
            existingGenresIds.add(genre.getId());
        }
        Set<Genre> updatedGenres = updatedFilm.getGenres();
        Set<Integer> updatedGenresIds = new TreeSet<>();
        for (Genre genre : updatedGenres) {
            updatedGenresIds.add(genre.getId());
        }

        Set<Integer> addedGenresIds = new TreeSet<>(updatedGenresIds);
        addedGenresIds.removeAll(existingGenresIds);

        Set<Integer> removedGenresIds = new TreeSet<>(existingGenresIds);
        removedGenresIds.removeAll(updatedGenresIds);

        for (Integer addedGenre : addedGenresIds) {
            genreDAO.addGenreToFilm(addedGenre, updatedFilm.getId());
        }

        for (Integer removedGenre : removedGenresIds) {
            genreDAO.deleteGenreFromFilm(removedGenre, updatedFilm.getId());
        }
    }
}
