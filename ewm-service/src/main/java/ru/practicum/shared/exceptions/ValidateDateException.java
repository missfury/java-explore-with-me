package ru.practicum.shared.exceptions_main;

public class ValidateDateException extends RuntimeException {

    public ValidateDateException(String message) {
        super(message);
    }
}