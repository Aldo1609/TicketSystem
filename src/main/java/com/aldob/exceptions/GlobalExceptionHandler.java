package com.aldob.exceptions;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.aldob.dtos.payload.ErrorResponse;
import com.aldob.exceptions.customs.CustomerNotActiveException;
import com.aldob.exceptions.customs.CustomerNotFoundException;
import com.aldob.exceptions.customs.InvalidTicketStatusTransitionException;
import com.aldob.exceptions.customs.TicketNotFoundException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(
            CustomerNotFoundException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.warn("Customer not found. traceId={}, path={}, detail={}",
                traceId, path(request), exception.getMessage());

        return errorResponse(
                HttpStatus.NOT_FOUND,
                "The requested resource was not found",
                traceId,
                request,
                Map.of()
        );
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(
            TicketNotFoundException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.warn("Ticket not found. traceId={}, path={}, detail={}",
                traceId, path(request), exception.getMessage());

        return errorResponse(
                HttpStatus.NOT_FOUND,
                "The requested resource was not found",
                traceId,
                request,
                Map.of()
        );
    }

    @ExceptionHandler(CustomerNotActiveException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(
            CustomerNotActiveException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.warn("Customer not found. traceId={}, path={}, detail={}",
                traceId, path(request), exception.getMessage());

        return errorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                "The requested resource was not found",
                traceId,
                request,
                Map.of()
        );
    }

    @ExceptionHandler(InvalidTicketStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTicketStatusTransition(
            InvalidTicketStatusTransitionException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.warn("Invalid ticket status transition. traceId={}, path={}, detail={}",
                traceId, path(request), exception.getMessage());

        return errorResponse(
                HttpStatus.CONFLICT,
                "The requested ticket status transition is not allowed",
                traceId,
                request,
                Map.of()
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(
            MissingRequestHeaderException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.warn("Missing required header. traceId={}, header={}",
                traceId, exception.getHeaderName());

        return errorResponse(
                HttpStatus.BAD_REQUEST,
                "The Idempotency-Key header is required",
                traceId,
                request,
                Map.of("Idempotency-Key", "The header is required")
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.warn(
            "Invalid request parameter. traceId={}, parameter={}, value={}",
            traceId,
            exception.getName(),
            exception.getValue()
        );

        return errorResponse(
            HttpStatus.BAD_REQUEST,
            "One or more request parameters have an invalid value",
            traceId,
            request,
            Map.of(exception.getName(), "The provided value is not valid")
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            WebRequest request
    ) {
        String traceId = traceId();
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );

        log.warn("Request validation failed. traceId={}, path={}, fields={}",
                traceId, path(request), errors.keySet());

        return errorResponse(
                HttpStatus.BAD_REQUEST,
                "The request contains invalid data",
                traceId,
                request,
                errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.warn("Constraint validation failed. traceId={}, path={}",
                traceId, path(request));

        return errorResponse(
                HttpStatus.BAD_REQUEST,
                "The request contains invalid data",
                traceId,
                request,
                Map.of()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(
            HttpMessageNotReadableException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.warn("Malformed request body. traceId={}, path={}",
                traceId, path(request));

        return errorResponse(
                HttpStatus.BAD_REQUEST,
                "The request body is invalid",
                traceId,
                request,
                Map.of()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.error("Database constraint violation. traceId={}, path={}",
                traceId, path(request), exception);

        return errorResponse(
                HttpStatus.CONFLICT,
                "The request could not be processed",
                traceId,
                request,
                Map.of()
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccess(
            DataAccessException exception,
            WebRequest request
    ) {
        String traceId = traceId();

        log.error("Database access error. traceId={}, path={}",
                traceId, path(request), exception);

        return errorResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "The service is temporarily unavailable",
                traceId,
                request,
                Map.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception exception,
            WebRequest request
    ) {
        String traceId = traceId();

        // El objeto exception como último argumento registra el stack trace completo.
        log.error("Unexpected error. traceId={}, method={}, path={}",
                traceId, method(request), path(request), exception);

        return errorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                traceId,
                request,
                Map.of()
        );
    }

    private ResponseEntity<ErrorResponse> errorResponse(
            HttpStatus status,
            String message,
            String traceId,
            WebRequest request,
            Map<String, String> errors
    ) {
        ErrorResponse response = new ErrorResponse(
                false,
                message,
                traceId,
                OffsetDateTime.now(),
                path(request),
                errors
        );

        return ResponseEntity.status(status).body(response);
    }

    private String traceId() {
        return UUID.randomUUID().toString();
    }

    private String path(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }
        return "unknown";
    }

    private String method(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getHttpMethod().name();
        }
        return "unknown";
    }
}
