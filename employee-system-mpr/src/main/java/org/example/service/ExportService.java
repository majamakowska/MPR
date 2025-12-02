package org.example.service;

import org.example.model.Employee;
import org.example.ports.EmployeeRepository;
import org.example.ports.FileSystem;
import org.example.ports.Formatter;

import java.util.List;

public class ExportService {
    private final EmployeeRepository employeeRepository;
    private final Formatter formatter;
    private final FileSystem fileSystem;

    public ExportService(EmployeeRepository employeeRepository, Formatter formatter, FileSystem fileSystem) {
        this.employeeRepository = employeeRepository;
        this.formatter = formatter;
        this.fileSystem = fileSystem;
    }

    public void export(String format, String path, boolean overwrite) {
        List<Employee> employees = employeeRepository.findAll();
        String content = formatter.formatEmployeesAs(format, employees);
        fileSystem.writeFile(path, content, overwrite);
    }
}