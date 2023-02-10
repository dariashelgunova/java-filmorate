package ru.yandex.practicum.filmorate.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"name", "description"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    Integer id;
    String name;
    String description;

}
