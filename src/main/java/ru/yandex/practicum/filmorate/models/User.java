package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;


@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    Integer id;
    @NotBlank(message = "Адрес электронной почты отсутствует. Попробуйте еще раз.")
    @Email(message = "Необходимо ввести электронную почту в соответствующем формате. Например - name@gmail.com")
    String email;
    @NotBlank(message = "Логин не может быть пустым")
    String login;
    String name;
    @NotNull
    @PastOrPresent(message = "Дата рождения не может относиться к будущему")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}
