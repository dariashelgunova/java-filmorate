package ru.yandex.practicum.filmorate.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import static java.lang.Integer.compare;

@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = "name")
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
