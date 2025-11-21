package com.ilias.review.controller;

import com.ilias.review.model.dto.ErrorResponseDto;
import com.ilias.review.model.dto.ValidationErrorDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdviceControllerTest {
    @InjectMocks
    private AdviceController advice;

    @Test
    void handleEntityNotFound_shouldReturn404() {
        EntityNotFoundException ex = new EntityNotFoundException("Book not found");

        ErrorResponseDto response = advice.handleEntityNotFound(ex);

        assertThat(response.status()).isEqualTo(404);
        assertThat(response.detail()).isEqualTo("Book not found");
    }

    @Test
    void handleValidationErrors_shouldReturnFieldErrors() throws Exception {

        Method method = Dummy.class.getMethod("dummy", DummyRequest.class);
        MethodParameter param = new MethodParameter(method, 0);

        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(new DummyRequest(), "request");
        binding.addError(new FieldError("request", "title", "must not be blank"));
        binding.addError(new FieldError("request", "rating", "must be >= 0"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(param, binding);

        List<ValidationErrorDto> errors = advice.handleValidationErrors(ex);

        assertThat(errors).hasSize(2);
        assertThat(errors.getFirst().field()).isEqualTo("title");
    }


    @Mock
    ConstraintViolation<?> violation;

    @Mock
    Path path;

    @Test
    void handleConstraintViolations_shouldMapViolationsCorrectly() {
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("book.title");
        when(violation.getMessage()).thenReturn("cannot be empty");

        ConstraintViolationException ex =
                new ConstraintViolationException(Set.of(violation));

        List<ValidationErrorDto> errors = advice.handleConstraintViolations(ex);

        assertThat(errors).hasSize(1);
        assertThat(errors.getFirst().field()).isEqualTo("book.title");
        assertThat(errors.getFirst().message()).isEqualTo("cannot be empty");
    }

    @Test
    void handleUnreadableJson_shouldReturn400() {
        HttpInputMessage inputMessage = mock(HttpInputMessage.class);
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("Malformed JSON", new RuntimeException(), inputMessage);

        ErrorResponseDto response = advice.handleUnreadableJson(ex);

        assertThat(response.status()).isEqualTo(400);
        assertThat(response.detail()).isEqualTo("Malformed JSON input.");
    }

    @Test
    void handleRestClientException_shouldReturn502() {
        RestClientException ex = new RestClientException("Bad Gateway");

        ErrorResponseDto response = advice.handleRestClientException(ex);

        assertThat(response.status()).isEqualTo(502);
    }

    @Test
    void handleHttpClientError_shouldReturnBadGateway() {
        HttpClientErrorException ex =
                HttpClientErrorException.create("Error", HttpStatus.BAD_REQUEST, "Bad Request", null, null, null);

        ErrorResponseDto response = advice.handleHttpClientError(ex);

        assertThat(response.status()).isEqualTo(502);
        assertThat(response.detail()).contains("External API returned HTTP 400");
    }

    @Test
    void handleOtherErrors_shouldReturn500() {
        Exception ex = new RuntimeException("Unexpected");

        ErrorResponseDto response = advice.handleOtherErrors(ex);

        assertThat(response.status()).isEqualTo(500);
        assertThat(response.detail()).isEqualTo("Internal server error.");
    }

    static class Dummy {
        public void dummy(DummyRequest req) { //dummy method for handleValidationErrors test
        }
    }

    static class DummyRequest {
        public String title;
        public Integer rating;
    }

}