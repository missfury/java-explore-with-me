package ru.practicum.ewmservice.shared.exceptions;

public class ValidateDateException extends RuntimeException {

    public ValidateDateException(String message) {
        super(message);
    }
}