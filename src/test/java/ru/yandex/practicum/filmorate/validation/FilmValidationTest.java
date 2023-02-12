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
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        film.setReleaseDate(LocalDate.of(2005, 10, 16));
        film.setDuration(55);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());

        String expectedMessage = "Название не может быть пустым";
        assertTrue(containsViolationWithMessage(expectedMessage, violations));
    }

    @Test
    public void testFilmWithWrongDescription() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                "descriptiondescriptiondescriptiondescription" );
        film.setReleaseDate(LocalDate.of(2005, 10, 16));
        film.setDuration(55);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        String expectedMessage = "Максимальная длина описания составляет 200 символов";
        assertTrue(containsViolationWithMessage(expectedMessage, violations));
    }

    @Test
    public void testFilmWithWrongReleasedate() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1700, 10, 16));
        film.setDuration(55);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        String expectedMessage = "Дата релиза должна быть не раньше 28 декабря 1895 года";
        assertTrue(containsViolationWithMessage(expectedMessage, violations));
    }

    @Test
    public void testFilmWithWrongDuration() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2005, 10, 16));
        film.setDuration(-55);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        String expectedMessage = "Длительность фильма не может быть отрицательной";
        assertTrue(containsViolationWithMessage(expectedMessage, violations));
    }

    private boolean containsViolationWithMessage(String expectedMessage, Set<ConstraintViolation<Film>> violations) {
        for (ConstraintViolation<Film> violation : violations) {
            if (expectedMessage.equals(violation.getMessage())) return true;
        }
        return false;
    }
}
