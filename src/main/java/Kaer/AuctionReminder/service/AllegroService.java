package Kaer.AuctionReminder.service;

import Kaer.AuctionReminder.model.Auction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AllegroService {
    private final RestTemplate restTemplate;
    @Value("${allegro.access-token}")
    private String accessToken;

    public AllegroService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Auction> getEndingAuctions() {
        String url = "https://api.allegro.pl/offers/listing?sellingMode.format=AUCTION&sort=endTime"; // Poprawny endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Accept", "application/vnd.allegro.public.v1+json"); // Poprawny nagłówek dla Allegro API

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("items")) {
            throw new RuntimeException("Nie udało się pobrać danych z API Allegro.");
        }

        List<Map<String, Object>> items = (List<Map<String, Object>>) ((Map<String, Object>) body.get("items")).get("regular");
        if (items == null) {
            throw new RuntimeException("Nie znaleziono aukcji w odpowiedzi API.");
        }

        return items.stream()
                .map(item -> new Auction(
                        (String) item.get("id"),
                        (String) item.get("name"),
                        (String) ((Map<String, Object>) item.get("endingTime")).get("value")
                ))
                .toList();
    }
}