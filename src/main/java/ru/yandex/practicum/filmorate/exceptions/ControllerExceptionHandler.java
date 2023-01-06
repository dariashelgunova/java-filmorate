package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = javax.validation.ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleValidationException(javax.validation.ValidationException ex) {
        throw new ValidationException(ex.getMessage());
    }

    @ExceptionHandler(value = NotFoundObjectException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleNotFoundObjectException(NotFoundObjectException ex) {
        throw new NotFoundObjectException(ex.getMessage());
    }

    @ExceptionHandler(value = UniversalException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleUniversalException(UniversalException ex) {
        throw new UniversalException(ex.getMessage());
    }
}
