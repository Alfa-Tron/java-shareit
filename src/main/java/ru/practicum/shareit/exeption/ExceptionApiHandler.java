package ru.practicum.shareit.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ExceptionApiHandler {


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(ValidationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage("Ошибка валидации"));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorMessage> handleConflictException(ConflictException exception) {
        ErrorMessage errorResponse = new ErrorMessage(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

}