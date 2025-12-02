package org.example.ports;

public interface CommunicationService {
    void sendReminder(String recipientEmail, String subject, String body);
}


