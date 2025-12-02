package org.example.ports;

import org.example.model.EmployeeCertificate;

import java.util.List;

public interface CertificateRepository {
    List<EmployeeCertificate> findCertificatesExpiringWithinDays(int days);
}