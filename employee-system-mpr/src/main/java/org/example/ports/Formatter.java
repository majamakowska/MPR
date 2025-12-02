package org.example.ports;

import org.example.model.Employee;

import java.util.List;

public interface Formatter {
    String formatEmployeesAs(String format, List<Employee> employees);
}