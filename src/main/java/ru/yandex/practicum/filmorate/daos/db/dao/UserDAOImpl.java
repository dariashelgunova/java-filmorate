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
import ru.yandex.practicum.filmorate.daos.UserDAO;
import ru.yandex.practicum.filmorate.daos.db.rowmappers.UserFieldsRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDAOImpl implements UserDAO {

    JdbcTemplate jdbcTemplate;
    UserFieldsRowMapper userFieldsRowMapper;

    @Override
    public Optional<User> findById(Integer id) {
        Optional<User> userById = findUserFieldsById(id);
        if (userById.isEmpty()) return Optional.empty();

        User user = userById.get();
        return Optional.of(user);
    }

    private Optional<User> findUserFieldsById(Integer id) {
        String findUserByIdSql =
                "select * " +
                "from users " +
                "where id = ?";
        List<User> result = jdbcTemplate.query(findUserByIdSql, userFieldsRowMapper, id);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.get(0));
    }

    @Override
    public List<User> findAll() {
        String findAllUsersIdsSql =
                "select id " +
                "from users";
        List<Integer> allUsersIds = jdbcTemplate.queryForList(findAllUsersIdsSql, Integer.class);
        List<User> allUsers = new ArrayList<>();

        for (Integer userId : allUsersIds) {
            Optional<User> byId = findById(userId);
            if (byId.isEmpty())
                throw new RuntimeException("Возникла внутренняя ошибка...");
            allUsers.add(byId.get());
        }
        allUsers.sort(Comparator.comparingInt(User::getId));
        return allUsers;
    }

    @Override
    public User create(User user) {
        int generatedId = insertNewUser(user);
        user.setId(generatedId);

        return user;
    }

    private int insertNewUser(User user) {
        String createUserSql =
                "insert into users (email, login, name, birthday) " +
                        "values (?, ?, ?, ?) ";


        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement statement = connection.prepareStatement(createUserSql, new String[]{"id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            LocalDate birthday = user.getBirthday();
            statement.setDate(4, Date.valueOf(birthday));
            return statement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        return requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public User update(User user) {
        Optional<User> userFromDb = findById(user.getId());
        if (userFromDb.isEmpty())
            throw new NotFoundObjectException(String.format("Пользователь с id %s не найден!", user.getId()));

        updateChangedUserFields(user);
        return user;
    }

    private void updateChangedUserFields(User updatedUser) {
        String updateUserSql =
                "update users " +
                "set email = ?, login = ?, name = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(con -> {
                    PreparedStatement statement = con.prepareStatement(updateUserSql);
                    statement.setString(1, updatedUser.getEmail());
                    statement.setString(2, updatedUser.getLogin());
                    statement.setString(3, updatedUser.getName());
                    LocalDate birthday = updatedUser.getBirthday();
                    statement.setDate(4, Date.valueOf(birthday));
                    statement.setInt(5, updatedUser.getId());
                    return statement;
                }
        );
    }
}
