package com.employees.employeesystemspringboot.service;

import com.employees.employeesystemspringboot.exception.DuplicateEmailException;
import com.employees.employeesystemspringboot.exception.EmployeeNotFoundException;
import com.employees.employeesystemspringboot.model.CompanyStatistics;
import com.employees.employeesystemspringboot.model.Employee;
import com.employees.employeesystemspringboot.model.EmploymentStatus;
import com.employees.employeesystemspringboot.model.Position;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final Set<Employee> employees = new HashSet<>();

    public void addEmployee(Employee employee) {
        if (!employees.add(employee)) {
            throw new DuplicateEmailException("Email powtarza się. Nie można dodać pracownika: " + employee);
        }
    }

    public void updateEmployee(String email, Employee employee) {
        employees.stream().filter(e -> e.getEmail().equals(email)).findFirst().ifPresent(employees::remove);
        addEmployee(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    public void removeByEmail(String email) {
        employees.stream().filter(e -> e.getEmail().equals(email)).findFirst().ifPresent(employees::remove);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public List<Employee> findByCompany(String company) {
        return employees.stream()
                .filter(e -> e.getCompanyName().equalsIgnoreCase(company)).toList();
    }

    public Employee findByEmail(String email) {
        Employee employee = employees.stream()
                .filter(e -> e.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
        if(employee == null) throw new EmployeeNotFoundException("Employee with email " + email + " not found.");
        return employee;
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

    public Map<EmploymentStatus, List<Employee>> groupByStatus() {
        return employees.stream().collect(Collectors.groupingBy(Employee::getStatus));
    }

    public Map<EmploymentStatus, Long> countByStatus() {
        return employees.stream().collect(Collectors.groupingBy(Employee::getStatus, Collectors.counting()));
    }

    public double averageSalary() {
        return averageSalary(new ArrayList<>(employees));
    }

    public double averageSalary(List <Employee> employees) {
        return employees.stream().mapToDouble(Employee::getSalary).average().orElse(0);
    }

    public Optional<Employee> highestSalaryEmployee() {
        return highestSalaryEmployee(new ArrayList<>(employees));
    }

    public Optional<Employee> highestSalaryEmployee(List <Employee> employees) {
        return employees.stream().max(Comparator.comparingDouble(Employee::getSalary));
    }

    public List<Employee> validateSalaryConsistency() {
        List<Employee> inconsistent = new ArrayList<>();
        employees.forEach(e -> {if (e.getSalary() < e.getPosition().getBaseSalary()) {inconsistent.add(e);}});
        return inconsistent;
    }

    public CompanyStatistics getCompanyStatisticsByCompanyName(String companyName) {
        List<Employee> employeeList = findByCompany(companyName);
        double avg = averageSalary(employeeList);
        Employee topEarner = highestSalaryEmployee(employeeList).get();
        return new CompanyStatistics(companyName, employeeList.size(),
                avg, topEarner.getSalary(), topEarner.getFullName());
    }

    public Map<String, CompanyStatistics> getCompanyStatistics() {
        Map<String, List<Employee>> employeesByCompany = employees.stream()
                .collect(Collectors.groupingBy(Employee::getCompanyName));

        Map<String, CompanyStatistics> result = new HashMap<>();
        employeesByCompany.forEach((company, employees) ->
            result.put(company, getCompanyStatisticsByCompanyName(company)));
        return result;
    }

    public Map<String, Double> getAverageSalaries() {
        Map<String, List<Employee>> employeesByCompany = employees.stream()
                .collect(Collectors.groupingBy(Employee::getCompanyName));

        Map<String, Double> result = new HashMap<>();
        employeesByCompany.forEach((company, employees) -> {
            double avg = averageSalary(employees);
            result.put(company, avg);
        });
        return result;
    }

    public Double getAverageSalaryByCompanyName(String company) {
        return averageSalary(employees.stream().filter(e -> Objects.equals(e.getCompanyName(), company)).toList());
    }

    public void deleteAllEmployees() {
        employees.clear();
    }
}