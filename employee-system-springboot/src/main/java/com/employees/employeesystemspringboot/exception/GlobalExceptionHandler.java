package com.employees.employeesystemspringboot.exception;

import com.employees.employeesystemspringboot.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage(), System.currentTimeMillis(), 404, request.getRequestURI()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException ex, HttpServletRequest request) {
        return ResponseEntity.status(409).body(new ErrorResponse(ex.getMessage(), System.currentTimeMillis(), 409, request.getRequestURI()));
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException ex, HttpServletRequest request) {
        return ResponseEntity.status(400).body(new ErrorResponse(ex.getMessage(), System.currentTimeMillis(), 400, request.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity.status(400).body(new ErrorResponse(ex.getMessage(), System.currentTimeMillis(), 400, request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(500).body(new ErrorResponse(ex.getMessage(), System.currentTimeMillis(), 500, request.getRequestURI()));
    }
}

