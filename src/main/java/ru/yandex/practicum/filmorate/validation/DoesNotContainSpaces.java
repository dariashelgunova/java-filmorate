package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StringDoesNotContainSpacesValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DoesNotContainSpaces {
    String message() default "Заданная строка сожержит пробельные символы!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
