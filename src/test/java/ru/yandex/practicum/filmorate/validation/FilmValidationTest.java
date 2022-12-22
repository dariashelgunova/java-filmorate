package ru.yandex.practicum.filmorate.validation;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.models.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmValidationTest {

    Validator validator;
    ValidatorFactory factory;

    @BeforeEach
    public void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    public void endUp() {
        factory.close();
    }

    @Test
    public void testFilmWithWrongName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("description");
        film.setReleaseDate(new Date(-737434800000L));
        film.setDuration(55);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        String message = "";
        for (ConstraintViolation<Film> violation : violations) {
            message = violation.getMessage();
        }

        assertEquals("Название не может быть пустым", message);
    }

    @Test
    public void testFilmWithWrongDescription() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                "descriptiondescriptiondescriptiondescription" );
        film.setReleaseDate(new Date(-737434800000L));
        film.setDuration(55);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        String message = "";
        for (ConstraintViolation<Film> violation : violations) {
            message = violation.getMessage();
        }

        assertEquals("Максимальная длина описания составляет 200 символов", message);
    }

    @Test
    public void testFilmWithWrongReleasedate() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("description");
        film.setReleaseDate(new Date(-5352575417000L));
        film.setDuration(55);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        String message = "";
        for (ConstraintViolation<Film> violation : violations) {
            message = violation.getMessage();
        }

        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года", message);
    }

    @Test
    public void testFilmWithWrongDuration() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(new Date(-737434800000L));
        film.setDuration(-55);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        String message = "";
        for (ConstraintViolation<Film> violation : violations) {
            message = violation.getMessage();
        }

        assertEquals("Длительность фильма не может быть отрицательной", message);
    }


}
