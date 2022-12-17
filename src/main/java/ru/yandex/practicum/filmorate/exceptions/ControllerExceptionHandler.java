package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = javax.validation.ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleValidationException(javax.validation.ValidationException ex) {
        throw new ValidationException(ex.getMessage());
    }
}
