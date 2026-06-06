package com.taskmanager.exception;

// Thrown for invalid input / duplicate data (400)
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
