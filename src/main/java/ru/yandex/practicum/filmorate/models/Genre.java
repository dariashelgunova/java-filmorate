package ru.yandex.practicum.filmorate.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static java.lang.Integer.compare;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Genre implements Comparable<Genre> {
    Integer id;
    String name;

    @Override
    public int compareTo(Genre o) {
        return compare(this.id, o.getId());
    }
}
