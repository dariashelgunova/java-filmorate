package ru.yandex.practicum.filmorate.daos.db.rowmappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import ru.yandex.practicum.filmorate.models.Genre;

@Value
@AllArgsConstructor
@Getter
public class FilmGenre {
    Integer filmId;
    Genre genre;
}
