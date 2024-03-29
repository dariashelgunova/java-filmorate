package ru.yandex.practicum.filmorate.daos.db.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.daos.FriendshipDAO;
import ru.yandex.practicum.filmorate.daos.db.rowmappers.UserFieldsRowMapper;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendshipDAOImpl implements FriendshipDAO {

    JdbcTemplate jdbcTemplate;
    UserFieldsRowMapper userFieldsRowMapper;

    public void addFriendship(Integer user1Id, Integer user2Id) {
        String addFriendshipSql =
                "insert into friendship (friend1_id, friend2_id) " +
                "values (?, ?)";
        jdbcTemplate.update(addFriendshipSql, user1Id, user2Id);
    }

    public void deleteFriendship(Integer user1Id, Integer user2Id) {
        String deleteFriendshipSql =
                "delete from friendship " +
                "where friend1_id = ? " +
                "and friend2_id = ?";
        jdbcTemplate.update(deleteFriendshipSql, user1Id, user2Id);
    }

    public List<User> findFriendsByUserId(Integer id) {
        String findUserFriendsSql =
                "select u.* " +
                "from friendship as f " +
                "left join users as u on f.friend2_id = u.id " +
                "where f.friend1_id = ?";
        return jdbcTemplate.query(findUserFriendsSql, userFieldsRowMapper, id);
    }

    public List<User> findCommonFriendsByUsersIds(int user1Id, int user2Id) {
        String findUserFriendsSql =
                "select * from users u, friendship f, friendship o " +
                "where u.id = f.friend2_id " +
                "AND u.id = o.friend2_id " +
                "AND f.friend1_id = ? " +
                "AND o.friend1_id = ?";
         return jdbcTemplate.query(findUserFriendsSql, userFieldsRowMapper, user1Id, user2Id);
    }
}
