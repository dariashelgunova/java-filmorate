package ru.yandex.practicum.filmorate.daos.db.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class FilmFieldsRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int mpaId = rs.getInt("mpa.id");
        String mpaName = rs.getString("mpa.name");
        String mpaDescription = rs.getString("mpa.description");
        Mpa mpa = new Mpa(mpaId, mpaName, mpaDescription);

        Integer id = rs.getInt("film.id");
        String name = rs.getString("film.name");
        String description = rs.getString("film.description");
        LocalDate releaseDate = rs.getDate("film.release_date").toLocalDate();
        int duration = rs.getInt("film.duration");
        int rate = rs.getInt("film.rate");
        Set<Genre> genres = new LinkedHashSet<>();
        return new Film(id, name, description, releaseDate, duration, rate,  genres, mpa);
    }
}
