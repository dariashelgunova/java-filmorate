package ru.yandex.practicum.filmorate.validation;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.models.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserValidationTest {

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
    public void testUserWithWrongEmail() {
        User user = new User();
        user.setBirthday(new Date(-737434800000L));
        user.setEmail("mailmail.ru");
        user.setName("Nick Name");
        user.setLogin("Dolore");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        String message = "";
        for (ConstraintViolation<User> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals("Необходимо ввести электронную почту в соответствующем формате. " +
                "Например - name@gmail.com", message);
    }

    @Test
    public void testUserWithEmptyLogin() {
        User user = new User();
        user.setBirthday(new Date(-737434800000L));
        user.setEmail("mail@mail.ru");
        user.setName("Nick Name");
        user.setLogin("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        String message = "";
        for (ConstraintViolation<User> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals("Логин не может быть пустым", message);
    }

    @Test
    public void testUserWithWrongBirthday() {
        User user = new User();
        user.setBirthday(new Date(1747774800000L));
        user.setEmail("mail@mail.ru");
        user.setName("Nick Name");
        user.setLogin("dolore");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        String message = "";
        for (ConstraintViolation<User> violation : violations) {
            message = violation.getMessage();
        }
        assertEquals("Дата рождения не может относиться к будущему", message);
    }

}
