package com.videogamescatalogue.backend.exception;

public class AccessNotAllowedException extends RuntimeException {
    public AccessNotAllowedException(String message) {
        super(message);
    }
}
