package ru.yandex.practicum.filmorate.storages.db.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserFieldsRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        Date birthday = rs.getObject("birthday", Date.class);
        return new User(id, email, login, name, birthday, null);
    }
}
