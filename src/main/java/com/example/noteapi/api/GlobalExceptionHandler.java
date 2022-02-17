package com.example.noteapi.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleUnmappedException(ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    private ResponseEntity<Object> handleUnmappedException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError apiError = ApiError.builder()
            .type(ex.getClass().getSimpleName())
//            .title(ex.getClass().getSimpleName())
            .status(status.value())
            .detail(ex.getMessage())
            .instance(UUID.randomUUID().toString())
            .build();
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    private ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = ex.getStatus();
        ApiError apiError = ApiError.builder()
            .type(ex.getClass().getSimpleName())
//            .title(ex.getClass().getSimpleName())
            .status(status.value())
            .detail(ex.getMessage())
            .instance(UUID.randomUUID().toString())
            .build();
        return new ResponseEntity<>(apiError, status);
    }
}
