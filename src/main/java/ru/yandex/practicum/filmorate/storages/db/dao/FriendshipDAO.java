package ru.yandex.practicum.filmorate.storages.db.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendshipDAO {

    JdbcTemplate jdbcTemplate;

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

    public List<Integer> findFriendsIdsByUserId(Integer id) {
        String findUserFriendsIdsSql =
                "select friend2_id " +
                        "from friendship " +
                        "where friend1_id = ?";
        return jdbcTemplate.queryForList(findUserFriendsIdsSql, Integer.class, id);
    }
}
