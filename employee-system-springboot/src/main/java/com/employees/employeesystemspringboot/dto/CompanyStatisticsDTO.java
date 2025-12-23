package com.employees.employeesystemspringboot.dto;

public record CompanyStatisticsDTO(
        String companyName,
        int employeeCount,
        double averageSalary,
        double highestSalary,
        String topEarnerFullName
) { }