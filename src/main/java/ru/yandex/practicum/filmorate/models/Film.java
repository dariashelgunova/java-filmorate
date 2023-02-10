package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {
    Integer id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @NotNull
    @Size(max = 200, message = "Максимальная длина описания составляет 200 символов")
    String description;
    @ReleaseDate
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    @NotNull
    LocalDate releaseDate;
    @Positive(message = "Длительность фильма не может быть отрицательной")
    int duration;
    int rate;
    @NotNull
    @JsonSerialize()
    Set<Genre> genres = new LinkedHashSet<>();
    @NotNull
    Mpa mpa;
}
