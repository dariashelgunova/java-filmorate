package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validation.DoesNotContainSpaces;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Data
public class User {

    private Integer id;
    @NotNull(message = "Адрес электронной почты отсутствует. Попробуйте еще раз.")
    @Email(message = "Необходимо ввести электронную почту в соответствующем формате. Например - name@gmail.com")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @DoesNotContainSpaces(message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может относиться к будущему")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
}
