package com.employees.employeesystemspringboot.service;

import com.employees.employeesystemspringboot.exception.ApiException;
import com.employees.employeesystemspringboot.exception.InvalidDataException;
import com.employees.employeesystemspringboot.model.Employee;
import com.employees.employeesystemspringboot.model.ImportSummary;
import com.employees.employeesystemspringboot.model.Position;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@ImportResource("classpath:employees-beans.xml")
public class ImportService {
    private final String csvFilePath;
    private final APIService apiService;
    private final List<Employee> predeterminedEmployees;

    public ImportService (@Value("${app.import.csv-file}") String csvFilePath, APIService apiService, @Qualifier("xmlEmployees") List<Employee> xmlEmployees) {
        this.csvFilePath = csvFilePath;
        this.predeterminedEmployees = xmlEmployees;
        this.apiService = apiService;
    }

    public ImportSummary importFromApi(EmployeeService employeeService) {
        try {
            return apiService.fetchEmployeesFromApi(employeeService);
        } catch (Exception e){
            throw new ApiException(e.getMessage());
        }
    }

    public ImportSummary importFromCsv(EmployeeService employeeService) {
        List<String> errors = new ArrayList<>();
        int imported = 0;

        try {
            File csvFile = new DefaultResourceLoader().getResource("classpath:" + csvFilePath).getFile();
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1) continue;

                try {
                    String[] parts = line.split(",");
                    if (parts.length < 5) {
                        throw new IllegalArgumentException("Za mało argumentów");
                    }

                    String firstName = parts[0].trim();
                    String lastName = parts[1].trim();
                    String email = parts[2].trim();
                    String companyName = parts[3].trim();
                    String positionString = parts[4].trim().toUpperCase();

                    if (Arrays.stream(Position.values()).noneMatch(p -> p.name().equals(positionString))) {
                        throw new InvalidDataException("Niepoprawna nazwa stanowiska");
                    }
                    Position position = Position.valueOf(positionString);

                    Employee fromFileEmployee = new Employee(firstName, lastName, email, companyName, position);

                    if (parts.length >= 6 && !parts[5].trim().isEmpty() && !parts[5].trim().equals("null")) {
                        double salary = Double.parseDouble(parts[5].trim());
                        if (salary <= 0) throw new IllegalArgumentException("Wynagrodzenie musi być dodatnie");
                        fromFileEmployee.setSalary(salary);
                    }

                    employeeService.addEmployee(fromFileEmployee);
                    imported++;

                } catch (Exception e) {
                    errors.add("Linia " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            errors.add("Błąd odczytu pliku: " + e.getMessage());
        }
        return new ImportSummary(imported, errors);
    }

    public ImportSummary importFromXML(EmployeeService employeeService) {
        List<String> errors = new ArrayList<>();
        int imported = 0;
        for (Employee pEmployee : predeterminedEmployees) {
            try {
                employeeService.addEmployee(pEmployee);
                imported++;
            } catch (Exception e) {
                errors.add(e.getMessage());
            }
        }
        return new ImportSummary(imported, errors);
    }
}