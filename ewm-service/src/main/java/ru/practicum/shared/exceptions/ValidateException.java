package ru.practicum.shared.exceptions_main;

public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }
}