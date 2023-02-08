package ru.yandex.practicum.filmorate.storages.db.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}
