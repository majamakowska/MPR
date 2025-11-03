package org.example.service;

import com.opencsv.CSVReader;
import org.example.exception.InvalidDataException;
import org.example.model.*;

import java.io.*;
import java.util.*;

public class ImportService {
    private final EmployeeService employeeService;

    public ImportService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public ImportSummary importFromCsv(String filePath) {
        List<String> errors = new ArrayList<>();
        int imported = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
}