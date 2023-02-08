package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.serializer.SortedSetJsonSerializer;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;


@Getter
@Setter
@EqualsAndHashCode(exclude = "likes")
@ToString(exclude = "likes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    Integer id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @NotNull
    @Size(max = 200, message = "Максимальная длина описания составляет 200 символов")
    String description;
    @ReleaseDate
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Moscow")
    @NotNull
    Date releaseDate;
    @Positive(message = "Длительность фильма не может быть отрицательной")
    int duration;
    @JsonIgnoreProperties("likes")
    Set<Integer> likes = new TreeSet<>();
    int rate;
    @NotNull
    @JsonSerialize(using = SortedSetJsonSerializer.class)
    Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
    @NotNull
    Mpa mpa;
}
