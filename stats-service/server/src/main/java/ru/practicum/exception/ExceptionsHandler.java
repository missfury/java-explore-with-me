package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ValidationException e) {
        log.error("Error Validation Exception: {}, {}", HttpStatus.BAD_REQUEST, e.getMessage());
        return Map.of("error", "Validation Error", "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Код ошибки: {}, {}", HttpStatus.BAD_REQUEST, e.getMessage());
        return Map.of("error", "Validation Error", "errorMessage", e.getMessage());
    }

}