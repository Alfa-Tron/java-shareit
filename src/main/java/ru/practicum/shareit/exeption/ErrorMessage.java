package ru.practicum.shareit.exeption;

import lombok.Data;

@Data
public class ErrorMessage {

    private String errors;


    public ErrorMessage(String errorMessages) {
        errors = errorMessages;
    }
}
