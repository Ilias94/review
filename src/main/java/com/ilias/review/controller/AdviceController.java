package com.ilias.review.controller;

import com.ilias.review.model.dto.ErrorResponseDto;
import com.ilias.review.model.dto.ValidationErrorDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found", ex);
        return problem(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationErrorDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.warn("Validation error", ex);

        return ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::mapValidationError)
                .toList();
    }

    private ValidationErrorDto mapValidationError(ObjectError error) {
        String field = error instanceof FieldError fe ? fe.getField() : error.getObjectName();
        return new ValidationErrorDto(field, error.getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationErrorDto> handleConstraintViolations(ConstraintViolationException ex) {
        log.warn("Constraint violation", ex);

        return ex.getConstraintViolations()
                .stream()
                .map(v -> new ValidationErrorDto(
                        v.getPropertyPath().toString(),
                        v.getMessage()
                ))
                .toList();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleUnreadableJson(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON", ex);
        return problem(HttpStatus.BAD_REQUEST, "Malformed JSON input.");
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponseDto handleRestClientException(RestClientException ex) {
        log.error("External API returned invalid response", ex);
        return problem(HttpStatus.BAD_GATEWAY, "Invalid response from external API.");
    }

    @ExceptionHandler(HttpServerErrorException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponseDto handleHttpServerError(HttpServerErrorException ex) {
        log.error("External API server error: {}", ex.getMessage());
        return problem(HttpStatus.BAD_GATEWAY,
                "External API returned HTTP " + ex.getStatusCode().value());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleOtherErrors(Exception ex) {
        log.error("Unexpected error", ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
    }


    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponseDto handleHttpClientError(HttpClientErrorException ex) {
        log.error("HTTP error during external API call: {}", ex.getMessage());

        return problem(HttpStatus.BAD_GATEWAY,
                "External API returned HTTP " + ex.getStatusCode().value());
    }


    private ErrorResponseDto problem(HttpStatus status, String detail) {
        return new ErrorResponseDto(
                status.value(),
                status.getReasonPhrase(),
                detail,
                LocalDateTime.now()
        );
    }
}
