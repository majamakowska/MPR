package com.employees.employeesystemspringboot.model;

import com.employees.employeesystemspringboot.dto.EmployeeDTO;

import java.util.Objects;

public class Employee {
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private Position position;
    private double salary;
    private EmploymentStatus status;

    public Employee(String firstName, String lastName, String email, String companyName,
                    Position position, double salary, EmploymentStatus status) {
        if (salary < 0) {
            throw new IllegalArgumentException("Pensja nie może być ujemna. "
                    + cannotAddMessage(firstName, lastName, email, companyName, position, salary, status));
        }
        try {
            this.firstName = capitalizeNames(validateStringData(firstName, "'imię'"));
            this.lastName = capitalizeNames(validateStringData(lastName, "'nazwisko'"));
            this.email = validateStringData(email, "'email'");
            this.companyName = capitalizeNames(validateStringData(companyName, "'nazwa firmy'"));
            this.position = ensureNotNullPosition(position);
            this.salary = salary;
            this.status = status;
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage()
                    + cannotAddMessage(firstName, lastName, email, companyName, position, salary, status));
        }
    }

    public Employee(String firstName, String lastName, String email, String companyName, Position position, double salary) {
        this(firstName, lastName, email, companyName, position, salary, EmploymentStatus.ACTIVE);
    }

    public Employee(String firstName, String lastName, String email, String companyName, Position position) {
        this(firstName, lastName, email, companyName, position,
                (position == null ? 0 : position.getBaseSalary()));
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Position getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    public EmploymentStatus getStatus() {
        return status;
    }

    public void setPosition(Position position) {
        this.position = ensureNotNullPosition(position);
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Pensja nie może być ujemna");
        } else {
            this.salary = salary;
        }
    }

    public void setStatus(EmploymentStatus status) {
        this.status = ensureNotNullStatus(status);
    }

    public EmployeeDTO toDTO() {
        return new EmployeeDTO(
                getFirstName(),
                getLastName(),
                getEmail(),
                getCompanyName(),
                getPosition(),
                getSalary(),
                getStatus()
        );
    }

    public static Employee fromDto(EmployeeDTO dto) {
        return new Employee(dto.firstName(), dto.lastName(), dto.email(), dto.companyName(), dto.position(), dto.salary(), dto.status());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != Employee.class) return false;
        Employee employee = (Employee) o;
        return email.equals(employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return '(' + firstName + " " + lastName + ", " + email + ", " + companyName + ", " + position + ", " + salary + ", " + status + ')';
    }

    public static String toString(String firstName, String lastName, String email, String companyName, Position position, double salary, EmploymentStatus status) {
        return '(' + firstName + " " + lastName + ", " + email + ", " + companyName + ", " + position + ", " + salary + ", " + status + ')';
    }

    private String validateStringData(String toValidate, String stringName) {
        stringName = (stringName == null) ? "" : stringName.trim().toLowerCase() + " ";
        if (toValidate == null) {
            throw new IllegalArgumentException("Wartość " + stringName + "nie może być null. ");
        }
        String validated = toValidate.trim();
        if (validated.isEmpty()) {
            throw new IllegalArgumentException("Pole " + stringName + "nie może być puste. ");
        }
        return validated;
    }

    private String capitalizeNames(String toCapitalize) {
        String[] names = toCapitalize.split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        for (String name : names) {
            if (!name.isEmpty()) {
                capitalized.append(name.substring(0, 1).toUpperCase()).append(name.substring(1).toLowerCase()).append(" ");
            }
        }
        return capitalized.toString().trim();
    }

    private static Position ensureNotNullPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Stanowisko nie może być null. ");
        }
        return position;
    }

    private static EmploymentStatus ensureNotNullStatus(EmploymentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status nie może być null. ");
        }
        return status;
    }

    private String cannotAddMessage(String firstName, String lastName, String email, String companyName, Position position, double salary, EmploymentStatus status) {
        return "Nie można dodać pracownika: " + Employee.toString(firstName, lastName, email, companyName, position, salary, status);
    }
}
