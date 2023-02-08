package ru.yandex.practicum.filmorate.storages.db.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.models.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class FilmFieldsRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Date releaseDate = rs.getObject("release_date", Date.class);
        int duration = rs.getInt("duration");
        int rate = rs.getInt("rate");
        return new Film(id, name, description, releaseDate, duration, null, rate,  null, null);
    }
}
