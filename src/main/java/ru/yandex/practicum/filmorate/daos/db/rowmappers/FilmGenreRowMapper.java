package ru.yandex.practicum.filmorate.daos.db.rowmappers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmGenreRowMapper implements RowMapper<FilmGenre> {
    Map<Integer, Genre> genreById;

    public FilmGenreRowMapper(Collection<Genre> allGenres) {
        this.genreById = allGenres.stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
    }

    @Override
    public FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FilmGenre(rs.getInt(1), genreById.get(rs.getInt(2)));
    }
}
