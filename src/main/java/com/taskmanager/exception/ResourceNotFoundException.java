package com.taskmanager.exception;

// Thrown when a resource is not found (404)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
