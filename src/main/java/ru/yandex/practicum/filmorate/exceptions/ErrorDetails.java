package ru.yandex.practicum.filmorate.exceptions;


import lombok.Value;

@Value
public class ErrorDetails {
    int status;
    String message;
}
