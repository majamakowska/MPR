package com.employees.employeesystemspringboot.dto;

import com.employees.employeesystemspringboot.model.EmploymentStatus;
import com.employees.employeesystemspringboot.model.Position;

public record EmployeeDTO(
        String firstName,
        String lastName,
        String email,
        String companyName,
        Position position,
        double salary,
        EmploymentStatus status
) { }
