package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Slf4j
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    private final static LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12,25);

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext cxt) {
        return date.isAfter(EARLIEST_RELEASE_DATE);
    }
}
