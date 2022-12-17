package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

@Slf4j
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, Date> {

    private final static Date EARLIEST_RELEASE_DATE = new Date(-2335573817000L);

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext cxt) {
        Date comparedDate = EARLIEST_RELEASE_DATE;
        return date.after(EARLIEST_RELEASE_DATE);
    }

}
