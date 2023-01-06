package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode(exclude = "likes")
@ToString(exclude = "likes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    Integer id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @Size(max = 200, message = "Максимальная длина описания составляет 200 символов")
    String description;
    @ReleaseDate
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date releaseDate;
    @Positive(message = "Длительность фильма не может быть отрицательной")
    int duration;
    Set<User> likes = new HashSet<>();
}
