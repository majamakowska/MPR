package org.example.testdoubles;

import org.example.ports.CommunicationService;

import java.util.ArrayList;
import java.util.List;

/** Spy: rejestruje wszystkie operacje wysłania powiadomienia, co pozwala sprawdzić ich liczbę i treść.*/

public class CommunicationServiceSpy implements CommunicationService {

    public static class Message {
        public final String to, subject, body;

        public Message(String to, String subject, String body){
            this.to = to;
            this.subject = subject;
            this.body = body;
        }
    }

    private final List<Message> sent = new ArrayList<>();

    @Override
    public void sendReminder(String recipientEmail, String subject, String body){
        sent.add(new Message(recipientEmail, subject, body));
    }
    public List<Message> getSent(){
        return List.copyOf(sent);
    }
}
