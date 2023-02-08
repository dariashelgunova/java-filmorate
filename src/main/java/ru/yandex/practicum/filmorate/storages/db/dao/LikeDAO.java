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
public class LikeDAO {

    JdbcTemplate jdbcTemplate;

    public void addLike(int userId, int filmId) {
        String addLikeSql =
                "insert into likes (user_id, film_id) " +
                        "values (?, ?)";
        jdbcTemplate.update(addLikeSql, userId, filmId);
    }

    public void deleteLike(int userId, int filmId) {
        String deleteLikeSql =
                "delete from likes " +
                        "where user_id = ? " +
                        "and film_id = ?";
        jdbcTemplate.update(deleteLikeSql, userId, filmId);
    }

    public List<Integer> getLikedUsersIdsByFilmId(Integer filmId) {
        String findLikesSql =
                "select user_id " +
                "from likes " +
                "where film_id = ?";
        return jdbcTemplate.queryForList(findLikesSql, Integer.class, filmId);
    }
}
