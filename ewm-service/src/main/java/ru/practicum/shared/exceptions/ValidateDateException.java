package ru.practicum.shared.exceptions;

public class ValidateDateException extends RuntimeException {

    public ValidateDateException(String message) {
        super(message);
    }
}