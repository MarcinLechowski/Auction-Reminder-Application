package Kaer.AuctionReminder.service;

import Kaer.AuctionReminder.model.Auction;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAuctionReminder(String email, Auction auction) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Aukcja zbliża się do końca!");
        message.setText("Aukcja " + auction.getTitle() + " kończy się o " + auction.getEndTime() +
                ". Link: https://allegro.pl/oferta/" + auction.getId());
        mailSender.send(message);
    }
}