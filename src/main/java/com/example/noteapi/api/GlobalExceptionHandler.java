package com.example.noteapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ApiError apiError = ApiError.builder()
            .type(ex.getClass().getSimpleName())
//            .title(ex.getClass().getSimpleName())
            .status(status.value())
            .detail(ex.getMessage())
            .instance(UUID.randomUUID().toString())
            .build();
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        Stream<ValidationErrorItem> objectErrors = ex.getGlobalErrors().stream()
            .map(error -> new ValidationErrorItem("*", messageSource.getMessage(error, locale)));
        Stream<ValidationErrorItem> fieldErrors = ex.getFieldErrors().stream()
            .map(error -> new ValidationErrorItem(error.getField(), messageSource.getMessage(error, locale)));
        List<ValidationErrorItem> validationErrors = Stream.concat(objectErrors, fieldErrors)
            .sorted()
            .toList();

        ApiError apiError = ApiError.builder()
            .type(ex.getClass().getSimpleName())
            .title("Validation failed")
            .status(status.value())
            .detail("Validation errors: " + validationErrors)
            .instance(UUID.randomUUID().toString())
            .validationErrors(validationErrors)
            .build();
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleBindException(ex, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    private ResponseEntity<Object> handleUnmappedException(Exception ex) {
        ResponseStatus responseStatus = AnnotatedElementUtils
            .findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        HttpStatus status = responseStatus != null
            ? responseStatus.value()
            : HttpStatus.INTERNAL_SERVER_ERROR;
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
