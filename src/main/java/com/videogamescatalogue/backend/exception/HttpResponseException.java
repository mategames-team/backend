package com.videogamescatalogue.backend.exception;

public class HttpResponseException extends RuntimeException {
    public HttpResponseException(String message) {
        super(message);
    }
}
