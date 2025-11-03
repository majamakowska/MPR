package org.example.model;

public class CompanyStatistics {
    private int employeeCount;
    private double averageSalary;
    private String topEarnerFullName;

    public CompanyStatistics(int employeeCount, double averageSalary, String topEarnerFullName) {
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.topEarnerFullName = topEarnerFullName;
    }

    @Override
    public String toString() {
        return String.format("Liczba pracowników: " + employeeCount  + ", Średnie wynagrodzenie: " + averageSalary
                + ", Najlepiej zarabiający pracownik: " + topEarnerFullName);
    }
}