package ru.yandex.practicum.filmorate.exceptions;

import lombok.Value;

@Value
public class FieldError {
    String fieldName;
    String message;
}
