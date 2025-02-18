package Kaer.AuctionReminder.controller;

import Kaer.AuctionReminder.model.Auction;
import Kaer.AuctionReminder.service.AllegroService;
import Kaer.AuctionReminder.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReminderController {
    private final AllegroService allegroService;
    private final NotificationService notificationService;

    public ReminderController(AllegroService allegroService, NotificationService notificationService) {
        this.allegroService = allegroService;
        this.notificationService = notificationService;
    }

    @GetMapping("/remind")
    public ResponseEntity<String> remind(@RequestParam String email, @RequestParam int minutesBeforeEnd) {
        try {
            List<Auction> auctions = allegroService.getEndingAuctions();
            if (auctions == null || auctions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Brak aukcji do powiadomienia.");
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            auctions.stream()
                    .filter(auction -> {
                        try {
                            LocalDateTime endTime = LocalDateTime.parse(auction.getEndTime(), formatter);
                            return endTime.isBefore(now.plusMinutes(minutesBeforeEnd));
                        } catch (Exception e) {
                            System.out.println("Błąd parsowania daty dla aukcji: " + auction.getId() + ", szczegóły: " + e.getMessage());
                            return false;
                        }
                    })
                    .forEach(auction -> {
                        try {
                            notificationService.sendAuctionReminder(email, auction);
                        } catch (Exception e) {
                            System.out.println("Błąd wysyłania powiadomienia dla aukcji: " + auction.getId() + ", szczegóły: " + e.getMessage());
                        }
                    });

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Powiadomienia wysłane na " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił błąd: " + e.getMessage());
        }
    }
}
