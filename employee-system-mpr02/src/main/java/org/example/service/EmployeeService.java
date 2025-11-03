package org.example.service;

import org.example.model.*;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        if (!employees.add(employee)) {
            throw new IllegalArgumentException("Email powtarza się. Nie można dodać pracownika: " + employee);
        }
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public List<Employee> findByCompany(String company) {
        return employees.stream()
                .filter(e -> e.getCompanyName().equalsIgnoreCase(company)).toList();
    }

    public List<Employee> sortByLastName() {
        return employees.stream().sorted(Comparator.comparing(e -> e.getLastName())).toList();
    }

    public Map<Position, List<Employee>> groupByPosition() {
        return employees.stream().collect(Collectors.groupingBy(Employee::getPosition));
    }

    public Map<Position, Long> countByPosition() {
        return employees.stream().collect(Collectors.groupingBy(Employee::getPosition, Collectors.counting()));
    }

    public double averageSalary() {
        return averageSalary(employees);
    }

    public double averageSalary(List <Employee> employees) {
        return employees.stream().mapToDouble(Employee::getSalary).average().orElse(0);
    }

    public Optional<Employee> highestSalaryEmployee() {
        return highestSalaryEmployee(employees);
    }

    public Optional<Employee> highestSalaryEmployee(List <Employee> employees) {
        return employees.stream().max(Comparator.comparingDouble(Employee::getSalary));
    }

    public List<Employee> validateSalaryConsistency() {
        List<Employee> inconsistent = new ArrayList<>();
        employees.forEach(e -> {if (e.getSalary() < e.getPosition().getBaseSalary()) {inconsistent.add(e);}});
        return inconsistent;
    }

    public Map<String, CompanyStatistics> getCompanyStatistics() {
        Map<String, List<Employee>> employeesByCompany = new HashMap<>();

        employeesByCompany = employees.stream().collect(Collectors.groupingBy(Employee::getCompanyName));

        Map<String, CompanyStatistics> result = new HashMap<>();

        employeesByCompany.forEach((company, employees) -> {
            double avg = averageSalary(employees);

            Employee topEarner = highestSalaryEmployee(employees).get();

            result.put(company, new CompanyStatistics(employees.size(), avg, topEarner.getFullName()));
        });

        return result;
    }
}
