package org.example.testdoubles;

import org.example.model.EmployeeCertificate;
import org.example.ports.CertificateRepository;

import java.util.List;

/** Stub: zwracaja przekazaną mu wcześniej listę wygasających certyyfikatów (niezależnie od parametru days).*/

public class CertificateRepositoryStub implements CertificateRepository {
    private final List<EmployeeCertificate> entries;

    public CertificateRepositoryStub(List<EmployeeCertificate> entries){
        this.entries = entries;
    }

    @Override
    public List<EmployeeCertificate> findCertificatesExpiringWithinDays(int days) {
        return entries;
    }
}