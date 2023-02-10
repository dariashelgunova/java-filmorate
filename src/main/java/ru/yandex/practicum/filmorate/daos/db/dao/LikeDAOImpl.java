package ru.yandex.practicum.filmorate.daos.db.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.daos.LikeDAO;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeDAOImpl implements LikeDAO {

    JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int userId, int filmId) {
        String addLikeSql =
                "insert into likes (user_id, film_id) " +
                        "values (?, ?)";
        jdbcTemplate.update(addLikeSql, userId, filmId);
        updateRate(filmId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        String deleteLikeSql =
                "delete from likes " +
                        "where user_id = ? " +
                        "and film_id = ?";
        jdbcTemplate.update(deleteLikeSql, userId, filmId);
        updateRate(filmId);
    }

    @Override
    public List<Integer> findLikedFilmsByUser(int userId) {
        String likesByUserSql =
                "select film_id " +
                "from likes " +
                "where user_id = ?";
        return jdbcTemplate.queryForList(likesByUserSql, Integer.class, userId);
    }

    @Override
    public List<Integer> findFilmsLikes(int filmId) {
        String likesByUserSql =
                "select count(user_id) " +
                        "from likes " +
                        "where film_id = ?";
        return jdbcTemplate.queryForList(likesByUserSql, Integer.class, filmId);
    }

    @Override
    public void updateRate(int filmId) {
        String sqlQuery =
                "update FILM f set rate = (select count(l.user_id) " +
                "from LIKES l where l.film_id = f.id)  " +
                "where f.id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}
