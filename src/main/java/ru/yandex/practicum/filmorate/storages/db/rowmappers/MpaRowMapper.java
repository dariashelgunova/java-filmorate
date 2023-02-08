package ru.yandex.practicum.filmorate.storages.db.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        return new Mpa(id, name, description);
    }
}
