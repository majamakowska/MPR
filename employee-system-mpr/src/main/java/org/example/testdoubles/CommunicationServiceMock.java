package org.example.testdoubles;

import org.example.ports.CommunicationService;

import java.util.ArrayList;
import java.util.List;

/** Mock: weryfikuje czy liczba wywołań sendReminder() zgadza się z oczekiwaną.*/

public class CommunicationServiceMock implements CommunicationService {
    public static class Message {
        public final String to, subject, body;

        public Message(String to, String subject, String body){
            this.to = to;
            this.subject = subject;
            this.body = body;
        }
    }
    private final List<Message> sent = new ArrayList<>();
    private Integer expectedCalls = null;

    @Override
    public void sendReminder(String recipientEmail, String subject, String body) {
        sent.add(new Message(recipientEmail, subject, body));
    }

    public void expectCalls(int n) {
        this.expectedCalls = n;
    }
    public void verify() {
        if(expectedCalls != null && sent.size() != expectedCalls) {
            throw new AssertionError("Expected sendReminder to be called " + expectedCalls + " times but was " + sent.size());
        }
    }
    public List<Message> getSent() {
        return List.copyOf(sent);
    }
}