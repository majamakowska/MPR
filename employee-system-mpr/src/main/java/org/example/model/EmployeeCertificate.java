package org.example.model;

import java.time.LocalDate;

public class EmployeeCertificate {
    private final Employee employee;
    private final String certificateType;
    private final LocalDate expiryDate;
    public EmployeeCertificate(Employee employee, String certificateType, LocalDate expiryDate) {
        this.employee = employee; this.certificateType = certificateType; this.expiryDate = expiryDate;
    }
    public Employee getEmployee(){ return employee; }
    public String getCertificateType(){ return certificateType; }
    public java.time.LocalDate getExpiryDate(){ return expiryDate; }
}