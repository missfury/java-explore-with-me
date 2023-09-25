package ru.practicum.ewmservice.shared.exceptions;

public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }
}