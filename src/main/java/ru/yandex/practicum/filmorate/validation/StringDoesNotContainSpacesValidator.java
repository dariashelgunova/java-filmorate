package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class StringDoesNotContainSpacesValidator implements ConstraintValidator<DoesNotContainSpaces, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.contains(" ");
    }
}
