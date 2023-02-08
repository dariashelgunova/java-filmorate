package ru.yandex.practicum.filmorate.storages.db.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;
import ru.yandex.practicum.filmorate.storages.db.rowmappers.UserFieldsRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDAO implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FriendshipDAO friendshipDAO;

    @Override
    public Optional<User> findById(Integer id) {
        Optional<User> userById = findUserFieldsById(id);
        if (userById.isEmpty()) return Optional.empty();

        User user = userById.get();
        addFriends(user);
        return Optional.of(user);
    }

    private Optional<User> findUserFieldsById(Integer id) {
        String findUserByIdSql =
                "select * " +
                "from users " +
                "where id = ?";
        List<User> result = jdbcTemplate.query(findUserByIdSql, new UserFieldsRowMapper(), id);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.get(0));
    }

    private void addFriends(User user) {
        List<Integer> userFriendsIds = friendshipDAO.findFriendsIdsByUserId(user.getId());
        List<User> userFriends = new ArrayList<>();

        for (Integer friendId : userFriendsIds) {
            Optional<User> userById = findUserFieldsById(friendId);
            if (userById.isEmpty())
                throw new NotFoundObjectException(String.format("Друг с id %s не найден!", friendId));
            userFriends.add(userById.get());
        }

        user.setFriends(userFriends);
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
        addDataAboutFriendships(user);

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
            statement.setDate(4, new Date(user.getBirthday().getTime()));
            return statement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        return requireNonNull(keyHolder.getKey()).intValue();
    }

    private void addDataAboutFriendships(User user) {
        List<User> friends = user.getFriends();
        if (friends == null) return;

        for (User friend : friends) {
            friendshipDAO.addFriendship(user.getId(), friend.getId());
        }
    }

    @Override
    public User update(User user) {
        Optional<User> userFromDb = findById(user.getId());
        if (userFromDb.isEmpty())
            throw new NotFoundObjectException(String.format("Пользователь с id %s не найден!", user.getId()));
        User fromDb = userFromDb.get();

        updateChangedUserFields(user);
        updateFriendships(fromDb, user);
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
                    statement.setDate(4, new Date(updatedUser.getBirthday().getTime()));
                    statement.setInt(5, updatedUser.getId());
                    return statement;
                }
        );
    }

    private void updateFriendships(User existingUser, User updatedUser) {
        List<User> existingFriends = existingUser.getFriends();
        List<User> updatedFriends = updatedUser.getFriends();

        List<User> addedFriends = new ArrayList<>(updatedFriends);
        addedFriends.removeAll(existingFriends);

        List<User> removedFriends = new ArrayList<>(existingFriends);
        removedFriends.removeAll(updatedFriends);

        for (User addedFriend : addedFriends) {
            friendshipDAO.addFriendship(existingUser.getId(), addedFriend.getId());
        }

        for (User removedFriend : removedFriends) {
            friendshipDAO.deleteFriendship(existingUser.getId(), removedFriend.getId());
        }
    }
}
