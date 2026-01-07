package com.videogamescatalogue.backend.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.info("Validation error", ex);
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::getErrorMessage)
                .toList();
        return new ResponseEntity<>(getBody(errors), headers,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex
    ) {
        log.info("Authentication error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessNotAllowedException.class)
    protected ResponseEntity<Object> handleAccessNotAllowedException(
            AccessNotAllowedException ex
    ) {
        log.info("AccessNotAllowed error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiException(
            ApiException ex
    ) {
        log.info("External API error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex
    ) {
        log.info("Entity Not Found error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpResponseException.class)
    protected ResponseEntity<Object> handleHttpResponseException(
            HttpResponseException ex
    ) {
        log.info("Http Response error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<Object> handleInvalidInputException(
            InvalidInputException ex
    ) {
        log.info("Invalid Input error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    protected ResponseEntity<Object> handleJwtAuthenticationException(
            JwtAuthenticationException ex
    ) {
        log.info("Jwt Authentication error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MappingException.class)
    protected ResponseEntity<Object> handleMappingException(
            MappingException ex
    ) {
        log.info("Mapping error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ObjectMapperException.class)
    protected ResponseEntity<Object> handleObjectMapperException(
            ObjectMapperException ex
    ) {
        log.info("Object Mapper error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ParsingException.class)
    protected ResponseEntity<Object> handleParsingException(
            ParsingException ex
    ) {
        log.info("Parsing error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(RegistrationException.class)
    protected ResponseEntity<Object> handleRegistrationException(
            RegistrationException ex
    ) {
        log.info("Registration error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SpecificationProviderNotFoundException.class)
    protected ResponseEntity<Object> handleSpecificationProviderNotFoundException(
            SpecificationProviderNotFoundException ex
    ) {
        log.info("Specification Provider Not Found error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationRequiredException.class)
    protected ResponseEntity<Object> handleAuthenticationRequiredException(
            AuthenticationRequiredException ex
    ) {
        log.info("Authentication Required error", ex);
        return new ResponseEntity<>(getBody(List.of(ex.getMessage())),
                HttpStatus.UNAUTHORIZED);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            return fieldError.getField() + ": " + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }

    private Map<String, Object> getBody(List<String> message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("errors", message);
        return body;
    }
}
