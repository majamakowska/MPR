package com.employees.employeesystemspringboot.model;

public class CompanyStatistics {
    private int employeeCount;
    private double averageSalary;
    private String topEarnerFullName;

    public CompanyStatistics(int employeeCount, double averageSalary, String topEarnerFullName) {
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.topEarnerFullName = topEarnerFullName;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public String getTopEarnerFullName() {
        return topEarnerFullName;
    }

    @Override
    public String toString() {
        return String.format("Liczba pracowników: " + employeeCount  + ", Średnie wynagrodzenie: " + averageSalary
                + ", Najlepiej zarabiający pracownik: " + topEarnerFullName);
    }
}