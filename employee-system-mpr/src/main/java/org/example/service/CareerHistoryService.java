package org.example.service;

import org.example.model.Employee;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CareerHistoryService {

    private final List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Nie można dodać null jako pracownika");
        }
        employees.add(employee);
    }

    public int calculateYearsWorked(Employee employee) {
        if (employee.getHireDate() == null) {
            return 0;
        }
        LocalDate hireDate = employee.getHireDate();
        LocalDate now = LocalDate.now();
        return Period.between(hireDate, now).getYears();
    }

    public List<Employee> getEmployeesWithAnniversary() {
        return employees.stream() .filter(e -> {
            int years = calculateYearsWorked(e);
            return years > 0 && years % 5 == 0;
        }) .toList();
    }

    public List<Employee> getEmployeesByYearsWorked(int minYears, int maxYears) {
        return employees.stream().filter(e -> calculateYearsWorked(e) >= minYears && calculateYearsWorked(e) <= maxYears).toList();
    }

    public List<Employee> getEmployeesSortedByExperience() {
        return employees.stream().
                sorted(Comparator.comparingInt(this::calculateYearsWorked)).toList();
    }

    public List<Employee> getEmployeesWithExactYearsWorked(int years) {
        return employees.stream()
                .filter(e -> calculateYearsWorked(e) == years) .toList();
    }
}
