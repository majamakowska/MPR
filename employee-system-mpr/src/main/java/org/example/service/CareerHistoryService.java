package org.example.service;

import org.example.model.Employee;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class CareerHistoryService {

    private final List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
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
        return employees.stream()
                .filter(e -> calculateYearsWorked(e) > 0 && calculateYearsWorked(e) % 5 == 0).toList();
    }

    public List<Employee> getEmployeesByYearsWorked(int minYears, int maxYears) {
        return employees.stream().filter(e -> calculateYearsWorked(e) >= minYears && calculateYearsWorked(e) <= maxYears).toList();
    }
}
