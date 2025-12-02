package org.example.service;

import org.example.model.EmployeeCertificate;
import org.example.ports.CertificateRepository;
import org.example.ports.CommunicationService;
import org.example.testdoubles.LoggerDummy;

import java.util.List;

public class TrainingReminderService {
    private final CertificateRepository certificateRepository;
    private final CommunicationService communicationService;
    private final LoggerDummy loggerDummy;

    public TrainingReminderService(CertificateRepository certificateRepository, CommunicationService communicationService, LoggerDummy loggerDummy) {
        this.certificateRepository = certificateRepository;
        this.communicationService = communicationService;
        this.loggerDummy = loggerDummy;
    }

    public void runDailyCheck(){
        List<EmployeeCertificate> expiring = certificateRepository.findCertificatesExpiringWithinDays(30);
        for(EmployeeCertificate ec : expiring){
            String to = ec.getEmployee().getEmail();
            String subject = "Przypomnienie o szkoleniu " + ec.getCertificateType();
            String body = String.format("Witaj %s,\nTwoje szkolenie %s wygasa %s. Prosimy o odnowienie.",
                    ec.getEmployee().getFirstName(), ec.getCertificateType(), ec.getExpiryDate());
            communicationService.sendReminder(to, subject, body);
        }
    }
}
