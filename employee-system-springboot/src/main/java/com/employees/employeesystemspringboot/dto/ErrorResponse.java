package com.employees.employeesystemspringboot.dto;

public record ErrorResponse(
        String message,
        long timestamp,
        int status,
        String path
) { }