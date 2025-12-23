package com.employees.employeesystemspringboot.model;

import com.employees.employeesystemspringboot.dto.CompanyStatisticsDTO;

public class CompanyStatistics {
    private String companyName;
    private int employeeCount;
    private double averageSalary;
    private double highestSalary;
    private String topEarnerFullName;

    public CompanyStatistics(String companyName, int employeeCount, double averageSalary, double highestSalary,
            String topEarnerFullName) {
        this.companyName = companyName;
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.highestSalary = highestSalary;
        this.topEarnerFullName = topEarnerFullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public double getHighestSalary() {
        return highestSalary;
    }

    public String getTopEarnerFullName() {
        return topEarnerFullName;
    }

    @Override
    public String toString() {
        return "Nazwa firmy: " + companyName + ", Liczba pracowników: " + employeeCount + ", Średnie wynagrodzenie: "
                + averageSalary + ", Najwyższe wynagrodzenie: " + highestSalary
                + ", Najlepiej zarabiający pracownik: " + topEarnerFullName;
    }

    public CompanyStatisticsDTO toDto() {
        return new CompanyStatisticsDTO(
                companyName,
                employeeCount,
                averageSalary,
                highestSalary,
                topEarnerFullName);
    }
}