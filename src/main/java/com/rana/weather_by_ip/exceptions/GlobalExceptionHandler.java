package com.rana.weather_by_ip.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(KeyNotFoundException.class)
    public ResponseEntity<CustomError> handleKeyNotFound(KeyNotFoundException ex, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", MDC.get("X-Correlation-ID"));
        Details details = new Details(ex.service, "Missing Environmental Variable", ex.suggestion);
        CustomError error = new CustomError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server error",
                ex.getMessage(),
                request.getRequestURI(),
                details
        );
        return new ResponseEntity<>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidIpAddressException.class)
    public ResponseEntity<CustomError> handleInvalidIp(InvalidIpAddressException ex, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", MDC.get("X-Correlation-ID"));
        Details details = new Details(ex.service, "Provided ip address failed validation or is missing.", ex.suggestion);
        CustomError error = new CustomError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Incorrect Details",
                ex.getMessage(),
                request.getRequestURI(),
                details
        );
        return new ResponseEntity<>(error, headers, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<CustomError> handleServiceUnavailability(ServiceUnavailableException ex, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", MDC.get("X-Correlation-ID"));
        Details details = new Details(ex.service, "Unreachable/Error Response", ex.suggestion);
        CustomError error = new CustomError(
                HttpStatus.BAD_GATEWAY.value(),
                "Bad Gateway",
                ex.getMessage(),
                request.getRequestURI(),
                details
        );
        return new ResponseEntity<>(error, headers, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(LimitExceededException.class)
    public ResponseEntity<CustomError> handleLimitExceeded(LimitExceededException ex, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", MDC.get("X-Correlation-ID"));
        Details details = new Details(ex.service, "Request Limit Exceeded", ex.suggestion);
        CustomError error = new CustomError(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too Many Requests",
                ex.getMessage(),
                request.getRequestURI(),
                details
        );
        return new ResponseEntity<>(error, headers, HttpStatus.TOO_MANY_REQUESTS);
    }

}
