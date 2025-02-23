package Kaer.AuctionReminder.service;

import Kaer.AuctionReminder.config.AllegroConfig;
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
import java.util.stream.Collectors;

@Service
public class AllegroService {
    private final RestTemplate restTemplate;
    private final AllegroConfig allegroConfig;

    public AllegroService(AllegroConfig allegroConfig) {
        this.restTemplate = new RestTemplate();
        this.allegroConfig = allegroConfig;
    }

    public List<Auction> getEndingAuctions() {
        // Budujemy URL wykorzystując adres z konfiguracji i sortując po publication.endingAt
        String url = allegroConfig.getApiUrl() + "/offers/listing?sellingMode.format=AUCTION&sort=publication.endingAt";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(allegroConfig.getAccessToken());
        headers.set("Accept", "application/vnd.allegro.public.v1+json");

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
        // Zakładamy strukturę: { "items": { "promoted": [...], "regular": [ ... ] } }
        Map<String, Object> itemsMap = (Map<String, Object>) body.get("items");
        List<Map<String, Object>> items = (List<Map<String, Object>>) itemsMap.get("regular");
        if (items == null) {
            throw new RuntimeException("Nie znaleziono aukcji w odpowiedzi API.");
        }
        return items.stream()
                .map(item -> {
                    String id = (String) item.get("id");
                    String title = (String) item.get("name");
                    // Pobieramy datę zakończenia z sekcji "publication.endingAt"
                    Map<String, Object> publication = (Map<String, Object>) item.get("publication");
                    String endingAt = (String) publication.get("endingAt");
                    return new Auction(id, title, endingAt);
                })
                .collect(Collectors.toList());
    }
}