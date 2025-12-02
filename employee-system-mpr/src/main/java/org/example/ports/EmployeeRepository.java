package org.example.ports;

import org.example.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    List<Employee> findAll();
}