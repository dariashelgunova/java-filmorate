package ru.yandex.practicum.filmorate.daos;

import java.util.List;

public interface LikeDAO {
    void addLike(int userId, int filmId);
    void deleteLike(int userId, int filmId);
    List<Integer> findLikedFilmsByUser(int userId);
    List<Integer> findFilmsLikes(int filmId);
}
