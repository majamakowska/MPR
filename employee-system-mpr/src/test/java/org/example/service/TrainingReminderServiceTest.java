package org.example.service;

import org.example.model.Employee;
import org.example.model.EmployeeCertificate;
import org.example.model.Position;
import org.example.testdoubles.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainingReminderServiceTest {

    Employee createEmployee(String firstName) {
        return new Employee(firstName, "Test", firstName.toLowerCase()+"@test", "Firma X", Position.PROGRAMISTA);
    }

    /** Używa:
     * - CertificateRepositoryStub zwracającego przekazaną mu wcześniej listę wygasających certyyfikatów,
     * - CommunicationServiceSpy zapisującego wszystkie operacje wysłania powiadomienia, co pozwala sprawdzić ich liczbę i treść,
     *
     * LoggerDummy nie jest w tym teście używany, jest tylko placeholderem, aby konstruktor był kompletny.
     *
     * Test sprawdza czy:
     * - jest wysyłane powiadomienie,
     * - czy jest wysyłane pod odpowiedni adres,
     * - czy ma odpowiednią treść */
    @Test
    void shouldSendRemindersForSingleExpiringCertificate() {
        Employee a = createEmployee("Anna");
        EmployeeCertificate ec1 = new EmployeeCertificate(a, "BHP", LocalDate.now().plusDays(10));
        CertificateRepositoryStub certificates = new CertificateRepositoryStub(List.of(ec1));
        CommunicationServiceSpy communicationServiceSpy = new CommunicationServiceSpy();

        TrainingReminderService service = new TrainingReminderService(certificates, communicationServiceSpy, new LoggerDummy());
        service.runDailyCheck();

        assertEquals(1, communicationServiceSpy.getSent().size());
        assertEquals("anna@test", communicationServiceSpy.getSent().get(0).to);
        assertTrue(communicationServiceSpy.getSent().get(0).body.contains("BHP"));
    }

    /** Używa:
     * - CertificateRepositoryStub,
     * - CommunicationServiceMock, weryfikującego czy liczba wywołań zgadza się z oczekiwaną,
     *
     * Test sprawdza czy:
     * - TrainingReminderService wysyła dokładnie dwa powiadomienia */
    @Test
    void shouldSendRemindersForMultipleExpiringCertificates() {
        Employee a = createEmployee("Anna");
        Employee b = createEmployee("Bartosz");
        EmployeeCertificate ec1 = new EmployeeCertificate(a, "RODO", LocalDate.now().plusDays(5));
        EmployeeCertificate ec2 = new EmployeeCertificate(b, "BHP", LocalDate.now().plusDays(15));
        CertificateRepositoryStub certificates = new CertificateRepositoryStub(List.of(ec1, ec2));
        CommunicationServiceMock communicationServiceMock = new CommunicationServiceMock();
        communicationServiceMock.expectCalls(2);

        TrainingReminderService service = new TrainingReminderService(certificates, communicationServiceMock, new LoggerDummy());
        service.runDailyCheck();

        communicationServiceMock.verify();
    }


    /** Używa:
     * - CertificateRepositoryStub,
     * - CommunicationServiceSpy,
     *
     *
     * Test sprawdza czy:
     * - nie jest wysyłana wiadomość, kiedy nie ma certyfikatów zbliżających się do wygaśnięcia */
    @Test
    void whenNoExpiringCerts_noMessagesSent() {
        CertificateRepositoryStub certificates = new CertificateRepositoryStub(List.of());
        CommunicationServiceSpy communicationServiceSpy = new CommunicationServiceSpy();
        TrainingReminderService service = new TrainingReminderService(certificates, communicationServiceSpy, new LoggerDummy());
        service.runDailyCheck();
        assertTrue(communicationServiceSpy.getSent().isEmpty());
    }
}
